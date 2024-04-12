package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface ScrollConfig extends Config {

    @Key("default.scrolls.number")
    @DefaultValue("10")
    int defaultScrollsNumber();

    @Key("scroll.one.time")
    @DefaultValue("1")
    int scrollOneTime();

    @Key("scroll.multiple.times")
    @DefaultValue("2")
    int scrollTwoTimes();

    @Key("scroll.three.times")
    @DefaultValue("3")
    int scrollThreeTimes();

    @Key("scroll.few.times")
    @DefaultValue("5")
    int scrollFewTimes();

    @Key("scroll.ten.times")
    @DefaultValue("10")
    int scrollTenTimes();

    @Key("scroll.many.times")
    @DefaultValue("12")
    int scrollManyTimes();

    @Key("scroll.lots.of.times")
    @DefaultValue("16")
    int scrollLotsOfTimes();

    @Key("scroll.really.long.time")
    @DefaultValue("25")
    int scrollReallyLongTime();
}
