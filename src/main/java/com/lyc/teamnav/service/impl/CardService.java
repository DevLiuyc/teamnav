package com.lyc.teamnav.service.impl;

import com.lyc.teamnav.bean.dto.CardDto;
import com.lyc.teamnav.bean.dto.CardIconDto;
import com.lyc.teamnav.bean.entity.Card;
import com.lyc.teamnav.bean.entity.Category;
import com.lyc.teamnav.bean.vo.CardTreeChildVo;
import com.lyc.teamnav.bean.vo.CardTreeVo;
import com.lyc.teamnav.bean.vo.CardVo;
import com.lyc.teamnav.common.constants.Constants;
import com.lyc.teamnav.common.exception.ResourceWriteException;
import com.lyc.teamnav.common.utils.BeanExtUtils;
import com.lyc.teamnav.common.utils.StringExtUtils;
import com.lyc.teamnav.repository.CardRepository;
import com.lyc.teamnav.service.cardtype.CardTypeServiceFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CardService {

    @Resource
    private CardRepository cardRepository;

    @Resource
    private CategoryService categoryService;

    @Resource
    private CardTypeServiceFactory cardTypeServiceFactory;

    @Resource
    private CommonService commonService;

    /**
     * 首页查询
     *
     * @param keywords keywords
     * @return List
     */
    public List<CardTreeVo> tree(String keywords) {
        List<Category> categories = categoryService.select();
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        List<Card> cards = StringUtils.isBlank(keywords)
                ? cardRepository.findAll() : cardRepository.findByKeywords(keywords.toLowerCase());
        if (CollectionUtils.isEmpty(cards)) {
            return Collections.emptyList();
        }
        Map<String, List<Card>> cardMap = cards.stream().collect(Collectors.groupingBy(Card::getCategory));
        return categories.stream()
                .map(category -> BeanExtUtils.convert(category, CardTreeVo::new)
                        .setChildren(setCardChildren(cardMap.get(category.getId()))))
                .filter(item -> CollectionUtils.isNotEmpty(item.getChildren()))
                .sorted(Comparator.comparing(CardTreeVo::getSort)).collect(Collectors.toList());
    }

    public List<CardTreeVo> treeByCategoryId(String id) {
        List<CardTreeVo> cardTreeVoList = this.tree("");
        if (CollectionUtils.isEmpty(cardTreeVoList)) {
            return null;
        }
        CardTreeVo cardTreeVo = cardTreeVoList.stream().collect(Collectors.toMap(CardTreeVo::getId, e -> e)).get(id);
        List<CardTreeVo> cardTreeVos = new ArrayList<>();
        if (Objects.nonNull(cardTreeVo)) {
            cardTreeVos.add(cardTreeVo);
        }
        return cardTreeVos;
    }

    private List<CardTreeChildVo> setCardChildren(List<Card> cardList) {
        if (CollectionUtils.isEmpty(cardList)) {
            return Collections.emptyList();
        }
        List<CardTreeChildVo> result = new ArrayList<>();
        for (Card card : cardList) {
            CardTreeChildVo vo = BeanExtUtils.convert(card, CardTreeChildVo::new);
            cardTypeServiceFactory.getService(card.getType()).formatCardVo(vo);
            vo.setTip(vo.getContent());
            result.add(vo);
        }
        result.sort(Comparator.comparing(CardTreeChildVo::getSort));
        return result;
    }

    /**
     * 根据分类查
     *
     * @param category category
     * @return List
     */
    public List<CardVo> select(String category) {
        List<Card> list = cardRepository.findByCategoryOrderBySortAsc(category);
        return list.stream().map(item -> {
            CardVo vo = BeanExtUtils.convert(item, CardVo::new);
            vo.setCategoryName(categoryService.get(item.getCategory()).getName());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 保存卡片
     *
     * @param id id
     * @param cardDto cardDto
     */
    public void save(String id, CardDto cardDto) {
        this.saveIcon(cardDto.getIcon());
        Card card = BeanExtUtils.convert(cardDto, Card::new);
        card.setId(StringUtils.isBlank(id) ? StringExtUtils.getUuid() : id);
        cardTypeServiceFactory.getService(card.getType()).supplySave(id, card);
        if (card.getSort() == null) {
            card.setSort(cardRepository.getMaxSort(card.getCategory()) + 1);
        }
        cardRepository.save(card);
    }

    private void saveIcon(CardIconDto cardIconDto) {
        if (!StringUtils.startsWith(cardIconDto.getSrc(), "http")) {
            return;
        }
        String path = StringExtUtils.format("/ext-resources/images/{}/{}.{}",
                DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()),
                StringExtUtils.getUuid(), FilenameUtils.getExtension(cardIconDto.getSrc()));
        File saveFile = new File(Constants.ROOT_DIR + path);
        try {
            FileUtils.forceMkdirParent(saveFile);
        } catch (IOException ex) {
            throw new ResourceWriteException("父目录生成失败", ex);
        }
        try (OutputStream outputStream = new FileOutputStream(saveFile)) {
            IOUtils.copy(new URL(cardIconDto.getSrc()), outputStream);
            cardIconDto.setSrc(path);
        } catch (Exception ex) {
            log.error("card icon 保存失败", ex);
        }
    }

    /**
     * 上移和下移，交换两个卡片序号
     *
     * @param category 分类ID
     * @param before 原来的索引
     * @param after 调整后的索引
     */
    public void changeSort(String category, int before, int after) {
        Supplier<List<Card>> supplier = () -> cardRepository.findByCategoryOrderBySortAsc(category);
        List<Card> updateList = commonService.changeSort(supplier, before, after);
        if (CollectionUtils.isEmpty(updateList)) {
            return;
        }
        cardRepository.saveAll(updateList);
    }

    /**
     * delete
     *
     * @param id id
     */
    public void delete(String id) {
        Card card = cardRepository.findById(id).orElseThrow(NullPointerException::new);
        cardRepository.deleteById(id);
        cardTypeServiceFactory.getService(card.getType()).supplyDelete(card);
    }

}
