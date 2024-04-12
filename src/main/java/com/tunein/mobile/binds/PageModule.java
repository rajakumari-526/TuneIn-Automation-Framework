package com.tunein.mobile.binds;

import com.google.inject.AbstractModule;
import com.tunein.mobile.binds.asserts.AndroidAssertsBinds;
import com.tunein.mobile.binds.asserts.IosAssertsBinds;
import com.tunein.mobile.binds.page.AndroidPageBinds;
import com.tunein.mobile.binds.page.IosPageBinds;
import com.tunein.mobile.binds.tool.AndroidToolBinds;
import com.tunein.mobile.binds.tool.IosToolBinds;
import com.tunein.mobile.binds.dialog.AndroidDialogBinds;
import com.tunein.mobile.binds.dialog.IosDialogBinds;

import static com.tunein.mobile.conf.ConfigLoader.config;

public class PageModule extends AbstractModule {

    private static final String ANDROID = "android";

    private static final String IOS = "ios";

    @Override
    protected void configure() {
        switch (config().mobileOS()) {
            case IOS -> {
                install(new IosPageBinds());
                install(new IosDialogBinds());
                install(new IosToolBinds());
                install(new IosAssertsBinds());
            }
            case ANDROID -> {
                install(new AndroidPageBinds());
                install(new AndroidDialogBinds());
                install(new AndroidToolBinds());
                install(new AndroidAssertsBinds());
            }
            default -> throw new Error("Unsupported platform");
        }
    }
}

