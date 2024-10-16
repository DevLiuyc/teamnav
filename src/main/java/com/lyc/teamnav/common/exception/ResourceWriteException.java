package com.lyc.teamnav.common.exception;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.lyc.teamnav.common.utils.StringExtUtils;

public class ResourceWriteException extends RuntimeException {

    private static final long serialVersionUID = 143422262865091780L;

    /**
     * ResourceReadException.
     *
     * @param message message
     * @param args args
     */
    public ResourceWriteException(String message, Object... args) {
        super(StringExtUtils.format(message, args), EventArgUtil.extractThrowable(args));
    }

}
