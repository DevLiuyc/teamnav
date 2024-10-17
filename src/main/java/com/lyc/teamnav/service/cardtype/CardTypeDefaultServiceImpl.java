package com.lyc.teamnav.service.cardtype;

import com.lyc.teamnav.bean.dto.CardIconDto;
import com.lyc.teamnav.bean.entity.Card;
import com.lyc.teamnav.bean.vo.CardTreeChildVo;
import com.lyc.teamnav.common.annotation.CardType;
import com.lyc.teamnav.common.enums.CardTypeEnum;
import com.lyc.teamnav.common.utils.FileExtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@CardType({CardTypeEnum.DEFAULT, CardTypeEnum.QRCODE})
public class CardTypeDefaultServiceImpl implements ICardTypeService {

    @Override
    public void formatCardVo(CardTreeChildVo cardVo) {
        // 默认的不用实现
    }

    @Override
    public void supplySave(String id, Card card) {
        // 默认的不用实现
    }

    @Override
    public void supplyDelete(Card card) {
        CardIconDto cardIconDto = card.getIcon();
        if (StringUtils.isNotBlank(cardIconDto.getSrc())
                && !StringUtils.contains(cardIconDto.getSrc(), "default")) {
            FileExtUtils.deleteFiles(false, cardIconDto.getSrc());
        }
    }

}
