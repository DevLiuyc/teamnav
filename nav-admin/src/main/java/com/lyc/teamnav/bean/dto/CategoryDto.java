package com.lyc.teamnav.bean.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CategoryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String icon;

    private String name;

    private Boolean privateCard;

    private Boolean valid;

    private Integer sort;

}
