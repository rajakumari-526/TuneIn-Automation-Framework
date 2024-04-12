package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface ProxyConfig extends Config {

    @Key("reporting.testing")
    @DefaultValue("false")
    Boolean isReportingTesting();

    @Key("decryption.by.applovin")
    @DefaultValue("false")
    Boolean isDecryptionByApplovin();

    @Key("proxy.port")
    @DefaultValue("8089")
    int proxyPort();

    @Key("proxy.host")
    @DefaultValue("0.0.0.0")
    String proxyHost();

    @Key("mitmdump.path")
    @DefaultValue("/opt/homebrew/bin/mitmdump")
    String mitmdumpPath();

    @Key("network.type")
    @DefaultValue("Wi-Fi")
    String networkType();

}
