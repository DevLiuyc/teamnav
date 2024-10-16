package com.lyc.teamnav.bean.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class CardDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 判断是原型还是普通
     */
    private String type;

    /**
     * 分类ID
     */
    private String category;

    /**
     * 结构化数据的icon
     */
    private CardIconDto icon;

    private String title;

    private String content;

    private String url;

    private CardZipDto zip;

    private Integer sort;

}
