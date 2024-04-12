package com.tunein.mobile.binds.tool;

import com.google.inject.AbstractModule;
import com.tunein.mobile.deviceactions.AndroidDeviceNativeActions;
import com.tunein.mobile.deviceactions.DeviceNativeActions;
import com.tunein.mobile.pages.alert.Alert;
import com.tunein.mobile.pages.alert.AndroidAlert;
import com.tunein.mobile.pages.android.navigation.AndroidNavigationAction;
import com.tunein.mobile.pages.common.navigation.NavigationAction;

public class AndroidToolBinds extends AbstractModule {

    protected void configure() {
        super.configure();
        bind(Alert.class).to(AndroidAlert.class).asEagerSingleton();
        bind(DeviceNativeActions.class).to(AndroidDeviceNativeActions.class).asEagerSingleton();
        bind(NavigationAction.class).to(AndroidNavigationAction.class).asEagerSingleton();
    }
}
