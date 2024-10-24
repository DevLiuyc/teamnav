package com.lyc.teamnav.common.annotation;

import com.lyc.teamnav.common.enums.CardTypeEnum;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CardType {

    /**
     * 数组，允许多个类型使用同一个实现
     *
     * @return CardTypeEnum
     */
    CardTypeEnum[] value();

}
