package com.lyc.teamnav.bean.dto;

import lombok.Data;

@Data
public class SettingDto {

    private Boolean nginxOpen;

    private String nginxUrl;

    private String navName;

    private String logoPath;

    private Integer cutOverSpeed;

    private Boolean logoToFavicon;

    private String layoutSize;

}
