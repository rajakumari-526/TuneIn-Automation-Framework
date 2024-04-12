package com.tunein.mobile.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import static com.tunein.mobile.conf.ConfigLoader.config;

public class LogbackLogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (config().isReportingTesting() && event.getLoggerName().contains("MitmproxyJava")) {
            return FilterReply.DENY;
        } else {
           return FilterReply.NEUTRAL;
        }
    }
}
