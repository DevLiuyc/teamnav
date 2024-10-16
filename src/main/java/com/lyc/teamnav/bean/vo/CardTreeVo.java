package com.lyc.teamnav.bean.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class CardTreeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String icon;

    private String name;

    private Integer sort;

    private List<CardTreeChildVo> children;
}
