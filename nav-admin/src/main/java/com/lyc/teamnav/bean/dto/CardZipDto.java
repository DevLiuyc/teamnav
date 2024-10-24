package com.lyc.teamnav.bean.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CardZipDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String path;

}
