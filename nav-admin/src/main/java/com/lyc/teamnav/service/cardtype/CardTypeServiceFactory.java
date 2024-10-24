package com.lyc.teamnav.service.cardtype;

import com.lyc.teamnav.common.annotation.CardType;
import com.lyc.teamnav.common.enums.CardTypeEnum;
import com.lyc.teamnav.common.utils.StringExtUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class CardTypeServiceFactory {

    @Resource
    private List<ICardTypeService> cardTypeServiceList;

    private Map<CardTypeEnum, ICardTypeService> cardTypeServiceMap;

    @PostConstruct
    private void init() {
        cardTypeServiceMap = new EnumMap<>(CardTypeEnum.class);
        for (ICardTypeService service : cardTypeServiceList) {
            CardType cardType = AnnotationUtils.findAnnotation(service.getClass(), CardType.class);
            if (cardType != null) {
                for (CardTypeEnum typeEnum : cardType.value()) {
                    cardTypeServiceMap.put(typeEnum, service);
                }
            }
        }
    }

    /**
     * getService
     *
     * @param type type
     * @return ICardTypeService
     */
    public ICardTypeService getService(String type) {
        CardTypeEnum cardTypeEnum = CardTypeEnum.getEnum(type);
        Assert.notNull(cardTypeEnum, "卡片类型参数无效");
        return Objects.requireNonNull(cardTypeServiceMap.get(cardTypeEnum),
                        StringExtUtils.format("未能实现卡片类型【{}】", type));
    }

}
