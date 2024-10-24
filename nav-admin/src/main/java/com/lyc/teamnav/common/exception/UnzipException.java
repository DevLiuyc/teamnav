package com.lyc.teamnav.common.exception;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.lyc.teamnav.common.utils.StringExtUtils;

public class UnzipException extends RuntimeException {

    private static final long serialVersionUID = -1150086217385159231L;

    /**
     * UnzipException.
     *
     * @param message message
     * @param args args
     */
    public UnzipException(String message, Object... args) {
        super(StringExtUtils.format(message, args), EventArgUtil.extractThrowable(args));
    }

}
