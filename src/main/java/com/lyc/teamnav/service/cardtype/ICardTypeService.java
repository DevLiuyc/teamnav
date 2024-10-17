package com.lyc.teamnav.service.cardtype;

import com.lyc.teamnav.bean.entity.Card;
import com.lyc.teamnav.bean.vo.CardTreeChildVo;

public interface ICardTypeService {

    /**
     * formatCard
     *
     * @param cardVo cardVo
     */
    void formatCardVo(CardTreeChildVo cardVo);

    /**
     * supplySave
     *
     * @param id id
     * @param card card
     */
    void supplySave(String id, Card card);

    /**
     * supplyDelete
     *
     * @param card card
     */
    void supplyDelete(Card card);

}
