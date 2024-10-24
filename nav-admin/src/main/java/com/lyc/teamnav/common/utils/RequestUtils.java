package com.lyc.teamnav.common.utils;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@UtilityClass
public class RequestUtils {

    /**
     * 得到reponse.
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return Optional.ofNullable(attrs)
                .map(ServletRequestAttributes::getResponse)
                .orElseThrow(() -> new UnsupportedOperationException("请在web上下文中获取HttpServletResponse"));
    }

    /**
     * 得到request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return Optional.ofNullable(attrs)
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> new UnsupportedOperationException("请在web上下文中获取HttpServletRequest"));
    }

    private static final String[] IE_AGENT = {"MSIE", "Trident", "Edge"};

    /**
     * isIe
     *
     * @return boolean
     */
    public static boolean isIe() {
        String userAgent = getRequest().getHeader("User-Agent");
        return StringUtils.containsAny(userAgent, IE_AGENT);
    }

}
