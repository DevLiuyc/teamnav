package com.lyc.teamnav.service.impl;


import com.lyc.teamnav.bean.dto.CategoryDto;
import com.lyc.teamnav.bean.entity.Category;
import com.lyc.teamnav.common.utils.BeanExtUtils;
import com.lyc.teamnav.common.utils.SecurityUtils;
import com.lyc.teamnav.common.utils.StringExtUtils;
import com.lyc.teamnav.repository.CardRepository;
import com.lyc.teamnav.repository.CategoryRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "categories")
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CardRepository cardRepository;

    private final CommonService commonService;

    /**
     * select
     *
     * @return List
     */
    public List<Category> select() {
        boolean isLogin = SecurityUtils.getUserInfo() != null;
        return categoryRepository.findAll(Example.of(new Category().setValid(true)),
                        Sort.by("sort")).stream()
                .filter(item -> isLogin || BooleanUtils.isFalse(item.getPrivateCard()))
                .collect(Collectors.toList());
    }

    /**
     * 分页查询无效分类
     *
     * @param keywords keywords
     * @param pageIndex pageIndex
     * @param pageSize pageSize
     * @return Page
     */
    public Page<Category> selectPage(String keywords, Integer pageIndex, Integer pageSize) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name",
                GenericPropertyMatchers.contains());
        return categoryRepository.findAll(Example.of(new Category().setValid(false).setName(keywords), matcher),
                PageRequest.of(pageIndex, pageSize, Sort.by("sort").descending()));
    }

    /**
     * get
     *
     * @param id id
     * @return Category
     */
    @Cacheable(key = "#id")
    public Category get(String id) {
        return categoryRepository.findById(id).orElse(new Category());
    }

    /**
     * 保存
     *
     * @param id id
     * @param dto dto
     * @return category
     */
    @CachePut(key = "#result.id")
    public Category save(String id, CategoryDto dto) {
        Category category = BeanExtUtils.convert(dto, Category::new);
        if (StringUtils.isBlank(id)) {
            category.setId(StringExtUtils.getUuid());
            category.setValid(true);
            category.setSort(categoryRepository.getMaxSort(true) + 1);
        } else {
            category.setId(id);
        }
        categoryRepository.save(category);
        return category;
    }

    /**
     * 删除
     *
     * @param id id
     */
    @CacheEvict(key = "#id")
    public void delete(String id) {
        categoryRepository.deleteById(id);
        cardRepository.deleteByCategoryId(id);
    }

    /**
     * 设为是否私密
     *
     * @param id id
     * @param privateCard privateCard
     */
    public void updatePrivate(String id, Boolean privateCard) {
        categoryRepository.updatePrivate(privateCard, Collections.singletonList(id));
    }

    /**
     * 修改排序
     *
     * @param before before
     * @param after after
     */
    public void changeSort(int before, int after) {
        Supplier<List<Category>> supplier = () -> categoryRepository.findAll(Example.of(
                new Category().setValid(true)), Sort.by("sort"));
        List<Category> updateList = commonService.changeSort(supplier, before, after);
        if (CollectionUtils.isEmpty(updateList)) {
            return;
        }
        categoryRepository.saveAll(updateList);
    }

    /**
     * 设置是否有效
     *
     * @param ids ids
     * @param valid valid
     */
    @Transactional(rollbackFor = Exception.class)
    public void setValid(List<String> ids, Boolean valid) {
        Assert.isTrue(!CollectionUtils.isEmpty(ids), "ids不能为空");
        int sort = categoryRepository.getMaxSort(valid) + 1;
        List<Category> categories = categoryRepository.findAllById(ids);
        for (Category category : categories) {
            category.setValid(valid).setSort(sort);
            sort++;
        }
        if (!CollectionUtils.isEmpty(categories)) {
            categoryRepository.saveAll(categories);
        }
        if (BooleanUtils.isFalse(valid)) {
            categoryRepository.updatePrivate(false, ids);
        }
    }

}
