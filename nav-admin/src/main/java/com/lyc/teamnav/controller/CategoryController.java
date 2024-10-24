package com.lyc.teamnav.controller;

import com.lyc.teamnav.bean.dto.CategoryDto;
import com.lyc.teamnav.bean.entity.Category;
import com.lyc.teamnav.service.impl.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 查询有效列表
     *
     * @return List
     */
    @GetMapping
    public ResponseEntity<List<Category>> select() {
        return ResponseEntity.ok(categoryService.select());
    }

    /**
     * 分页查询无效分类
     *
     * @param keywords keywords
     * @param pageIndex pageIndex
     * @param pageSize pageSize
     * @return Page
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Category>> selectPage(String keywords,
                                                     @RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(categoryService.selectPage(keywords, pageIndex, pageSize));
    }

    /**
     * add
     *
     * @param category category
     * @return Void
     */
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody CategoryDto category) {
        categoryService.save(null, category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * update
     *
     * @param id id
     * @param category category
     * @return Void
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody CategoryDto category) {
        categoryService.save(id, category);
        return ResponseEntity.noContent().build();
    }

    /**
     * update private
     *
     * @param id id
     * @param privateCard privateCard
     * @return Void
     */
    @PatchMapping("/{id}/private/{type}")
    public ResponseEntity<Void> updatePrivate(@PathVariable("id") String id,
                                              @PathVariable("type") Boolean privateCard) {
        categoryService.updatePrivate(id, privateCard);
        return ResponseEntity.noContent().build();
    }

    /**
     * delete
     *
     * @param id id
     * @return Void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * changeSort
     *
     * @param before before
     * @param after after
     * @return Void
     */
    @PatchMapping("/actions/sort")
    public ResponseEntity<Void> changeSort(@RequestParam Integer before, @RequestParam Integer after) {
        categoryService.changeSort(before, after);
        return ResponseEntity.noContent().build();
    }

    /**
     * setValid
     *
     * @param ids ids
     * @param valid valid
     * @return Void
     */
    @PatchMapping("/valid/{valid}")
    public ResponseEntity<Void> setValid(@RequestBody List<String> ids, @PathVariable Boolean valid) {
        categoryService.setValid(ids, valid);
        return ResponseEntity.noContent().build();
    }
}
