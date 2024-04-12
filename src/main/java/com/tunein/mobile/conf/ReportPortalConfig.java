package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface ReportPortalConfig extends Config {

    @Key("rp.enable")
    @DefaultValue("false")
    boolean isReportPortalEnabled();

    @Key("rp.token")
    String reportPortalToken();

    @Key("rp.project")
    @DefaultValue("mobile_automation")
    String reportPortalProject();

    @Key("store.rp.url.in.file")
    @DefaultValue("false")
    boolean storeReportPortalUrlInFile();
}
