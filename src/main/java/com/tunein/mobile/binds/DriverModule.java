package com.tunein.mobile.binds;

import com.google.inject.AbstractModule;
import com.tunein.mobile.appium.driverprovider.AndroidDriverProvider;
import com.tunein.mobile.appium.driverprovider.IosDriverProvider;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.codeborne.selenide.Configuration.browser;

public class DriverModule extends AbstractModule {
    private static final String ANDROID = "android";

    private static final String IOS = "ios";

    @Override
    protected void configure() {
        switch (config().mobileOS()) {
            case ANDROID -> {
                browser = AndroidDriverProvider.class.getName();
            }
            case IOS -> {
                browser = IosDriverProvider.class.getName();
            }
            default -> throw new Error("Unsupported platform");
        }
    }
}
