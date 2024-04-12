package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface TimeOutConfig extends Config {

    @Key("element.wait.long.timeout.seconds")
    @DefaultValue("35")
    int waitLongTimeoutSeconds();

    @Key("element.wait.long.timeout.milliseconds")
    @DefaultValue("20000")
    int waitLongTimeoutMilliseconds();

    @Key("element.wait.extra.long.timeout.milliseconds")
    @DefaultValue("35000")
    int waitExtraLongTimeoutMilliseconds();

    @Key("page.ready.timeout.seconds")
    @DefaultValue("10")
    int pageReadyTimeoutSeconds();

    @Key("element.visible.timeout.seconds")
    @DefaultValue("8")
    int elementVisibleTimeoutSeconds();

    @Key("element.wait.medium.timeout.seconds")
    @DefaultValue("12")
    int waitMediumTimeoutSeconds();

    @Key("element.wait.twenty.seconds.timeout")
    @DefaultValue("20")
    int waitTwentySecondsTimeout();

    @Key("element.wait.short.timeout.seconds")
    @DefaultValue("5")
    int waitShortTimeoutSeconds();

    @Key("element.not.visible.timeout.seconds")
    @DefaultValue("3")
    int elementNotVisibleTimeoutSeconds();

    @Key("element.wait.very.short.timeout.seconds")
    @DefaultValue("2")
    int waitVeryShortTimeoutSeconds();

    @Key("timeout.one.second")
    @DefaultValue("1")
    int timeoutOneSecond();

    @Key("custom.wait.timeout.milliseconds")
    @DefaultValue("10000")
    int waitCustomTimeoutMilliseconds();

    @Key("short.wait.timeout.milliseconds")
    @DefaultValue("5000")
    int waitShortTimeoutMilliseconds();

    @Key("very.short.wait.timeout.milliseconds")
    @DefaultValue("2000")
    int waitVeryShortTimeoutMilliseconds();

    @Key("default.scroll.duration")
    @DefaultValue("600")
    int defaultScrollDuration();

    @Key("refresh.scroll.duration")
    @DefaultValue("600")
    int refreshScrollDuration();

    @Key("one.minute.in.seconds")
    @DefaultValue("60")
    int oneMinuteInSeconds();

    @Key("fifteen.minutes.in.seconds")
    @DefaultValue("900")
    int fifteenMinutesInSeconds();

    @Key("two.minute.in.seconds")
    @DefaultValue("120")
    int twoMinuteInSeconds();

    @Key("one.minute.in.milliseconds")
    @DefaultValue("60000")
    int oneMinuteInMilliseconds();

    @Key("new.command.timeout.seconds")
    @DefaultValue("240")
    int newCommandTimeoutInSeconds();

    @Key("timeout.for.deeplink.milliseconds")
    @DefaultValue("1000")
    int timeoutForDeeplinkInMilliseconds();

    @Key("timeout.half.a.second")
    @DefaultValue("500")
    int timeoutHalfASecond();

    @Key("five.minutes.in.milliseconds")
    @DefaultValue("300000")
    int fiveMinuteInMilliseconds();

    @Key("two.minutes.in.milliseconds")
    @DefaultValue("120000")
    int twoMinutesInMilliseconds();

    @Key("timeout.beetween.rolls.in.seconds")
    @DefaultValue("40")
    int timeBetweenRollsInSeconds();

    @Key("timeout.for.stability.tests.in.seconds")
    @DefaultValue("600")
    int timeStabilityTestInSeconds();

    @Key("five.minute.timeout.in.seconds")
    @DefaultValue("300")
    int fiveMinutesInSeconds();

    @Key("four.minute.timeout.in.seconds")
    @DefaultValue("240")
    int fourMinutesInSeconds();

    @Key("twenty.minute.timeout.in.seconds")
    @DefaultValue("1200")
    int twentyMinutesInSeconds();

    @Key("ten.minute.timeout.in.seconds")
    @DefaultValue("600")
    int tenMinutesInSeconds();

    @Key("memory.monitoring.timeout.in.hours")
    @DefaultValue("6")
    int memoryMonitoringTimeoutInHours();

}
