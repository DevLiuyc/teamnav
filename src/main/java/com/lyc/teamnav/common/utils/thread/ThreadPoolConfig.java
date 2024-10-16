package com.lyc.teamnav.common.utils.thread;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "thread-pool")
public class ThreadPoolConfig {

    /**
     * 线程名前缀.
     */
    private String threadNamePrefix;

    /**
     * 核心线程数.
     */
    private Integer corePoolNum;

    /**
     * 最大线程数.
     */
    private Integer maxPoolNum;

    /**
     * 线程存活时间.
     */
    private Long keepAliveTime;

    /**
     * 队列大小.
     */
    private Integer queueSize;

}
