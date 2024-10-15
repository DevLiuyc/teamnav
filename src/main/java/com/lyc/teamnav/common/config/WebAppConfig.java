package com.lyc.teamnav.common.config;

import com.lyc.teamnav.common.constants.Constants;
import com.lyc.teamnav.common.utils.StringExtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // addResourceLocations 中的参数，必须以【/】结尾!!!
        registry.addResourceHandler("/ext-resources/**")
                .addResourceLocations(StringExtUtils.format("file:{}/ext-resources/",
                        Constants.ROOT_DIR));

    }

}
