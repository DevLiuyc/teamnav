package com.lyc.teamnav.service.impl;

import com.lyc.teamnav.bean.dto.CardDto;
import com.lyc.teamnav.bean.dto.CardIconDto;
import com.lyc.teamnav.bean.entity.Card;
import com.lyc.teamnav.bean.vo.CardTreeChildVo;
import com.lyc.teamnav.bean.vo.CardTreeVo;
import com.lyc.teamnav.bean.vo.CardVo;
import com.lyc.teamnav.service.ICardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements ICardService {
    @Override
    public List<CardTreeVo> tree(String keywords) {
        return null;
    }

    @Override
    public List<CardTreeChildVo> setCardChildren(List<Card> cardList) {
        return null;
    }

    @Override
    public List<CardVo> select(String category) {
        return null;
    }

    @Override
    public void save(String id, CardDto cardDto) {

    }

    @Override
    public void saveIcon(CardIconDto cardIconDto) {

    }

    @Override
    public void changeSort(String category, int before, int after) {

    }

    @Override
    public void delete(String id) {

    }
}
