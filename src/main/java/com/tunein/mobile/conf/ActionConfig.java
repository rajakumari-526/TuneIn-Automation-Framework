package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface ActionConfig extends Config {

    @Key("default.swipe.distance")
    @DefaultValue("0.5")
    float defaultSwipeDistance();

    @Key("now.playing.swipe.distance")
    @DefaultValue("0.72")
    float nowPlayingSwipeDistance();

    @Key("now.playing.scroll.distance")
    @DefaultValue("0.5")
    float nowPlayingScrollDistance();

}
