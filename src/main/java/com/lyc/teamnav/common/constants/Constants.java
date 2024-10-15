package com.lyc.teamnav.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    /**
     * API 版本1的前缀.
     */
    public static final String API_V1 = "/api/v1";

    /**
     * 与jar同级目录，jar如果放在系统根目录，比如docker环境
     * System.getProperty("user.dir")获取到的是"/"，直接去掉.
     */
    public static final String ROOT_DIR = "/".equals(System.getProperty("user.dir")) ? ""
            : System.getProperty("user.dir");

    /**
     * 默认的logo路径
     */
    public static final String DEFAULT_LOGO_PATH = "/assets/images/logo.png";

    /**
     * 获取favicon超时时间
     */
    public static final int FAVICON_TIMEOUT = 5000;

}
