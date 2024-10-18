package com.lyc.teamnav.controller;

import com.lyc.teamnav.bean.dto.CardDto;
import com.lyc.teamnav.bean.vo.CardTreeVo;
import com.lyc.teamnav.bean.vo.CardVo;
import com.lyc.teamnav.service.impl.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    /**
     * 首页获取树结构的卡片
     *
     * @param keywords 卡片关键字搜索
     * @return List
     */
    @GetMapping("/card/tree")
    public ResponseEntity<List<CardTreeVo>> tree(String keywords) {
        List<CardTreeVo> result = cardService.tree(keywords);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/card/treeById")
    public ResponseEntity<List<CardTreeVo>> treeByCategoryId(String categoryId) {
        List<CardTreeVo> cardTreeVoList = cardService.tree(categoryId);
        return ResponseEntity.ok(cardTreeVoList);
    }

    /**
     * 卡片维护列表根据分类编号查询
     *
     * @param category category
     * @return List
     */
    @GetMapping("/category/{category}/card")
    public ResponseEntity<List<CardVo>> select(@PathVariable("category") String category) {
        return ResponseEntity.ok(cardService.select(category));
    }

    /**
     * 新增卡片
     *
     * @param cardDto 新增模型数据
     * @return Void
     */
    @PostMapping("/card")
    public ResponseEntity<Void> add(@RequestBody CardDto cardDto) {
        cardService.save(null, cardDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改卡片
     *
     * @param id id
     * @param cardDto 修改数据模型
     * @return Void
     */
    @PatchMapping("/card/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody CardDto cardDto) {
        cardService.save(id, cardDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 排序
     *
     * @param category 分类id
     * @param before 原来排序
     * @param after 现在的排序
     * @return Void
     */
    @PatchMapping("/category/{category}/card/actions/sort")
    public ResponseEntity<Void> changeSort(@PathVariable("category") String category,
                                           @RequestParam("before") Integer before,
                                           @RequestParam("after") Integer after) {
        cardService.changeSort(category, before, after);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据ID删除一个卡片
     *
     * @param id id
     * @return Void
     */
    @DeleteMapping("/card/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
