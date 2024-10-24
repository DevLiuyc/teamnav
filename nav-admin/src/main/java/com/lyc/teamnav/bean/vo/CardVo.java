package com.lyc.teamnav.bean.vo;

import com.lyc.teamnav.bean.dto.CardIconDto;
import com.lyc.teamnav.bean.dto.CardZipDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CardVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String type;

    private String category;

    private String categoryName;

    private CardIconDto icon;

    private String title;

    private String content;

    private String url;

    private CardZipDto zip;

    private Integer sort;
}
