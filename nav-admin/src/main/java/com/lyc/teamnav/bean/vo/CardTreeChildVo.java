package com.lyc.teamnav.bean.vo;

import com.lyc.teamnav.bean.dto.CardIconDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CardTreeChildVo {

    private String id;

    private String type;

    private CardIconDto icon;

    private String title;

    private String content;

    private String url;

    private String tip;

    private Integer sort;
}
