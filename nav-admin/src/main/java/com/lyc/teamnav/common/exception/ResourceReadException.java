package com.lyc.teamnav.common.exception;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.lyc.teamnav.common.utils.StringExtUtils;

public class ResourceReadException extends RuntimeException {

    /**
     * ResourceReadException.
     *
     * @param message message
     * @param args args
     */
    public ResourceReadException(String message, Object... args) {
        super(StringExtUtils.format(message, args), EventArgUtil.extractThrowable(args));
    }

}
