package com.lyc.teamnav.service;

import com.lyc.teamnav.bean.dto.CardDto;
import com.lyc.teamnav.bean.dto.CardIconDto;
import com.lyc.teamnav.bean.entity.Card;
import com.lyc.teamnav.bean.vo.CardTreeChildVo;
import com.lyc.teamnav.bean.vo.CardTreeVo;
import com.lyc.teamnav.bean.vo.CardVo;

import java.util.List;

public interface ICardService {

    List<CardTreeVo> tree(String keywords);

    List<CardTreeChildVo> setCardChildren(List<Card> cardList);

    List<CardVo> select(String category);

    void save(String id, CardDto cardDto);

    void saveIcon(CardIconDto cardIconDto);

    void changeSort(String category, int before, int after);

    void delete(String id);
}
