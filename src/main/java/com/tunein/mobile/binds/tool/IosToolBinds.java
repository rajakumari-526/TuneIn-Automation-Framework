package com.tunein.mobile.binds.tool;

import com.google.inject.AbstractModule;
import com.tunein.mobile.deviceactions.DeviceNativeActions;
import com.tunein.mobile.deviceactions.IosDeviceNativeActions;
import com.tunein.mobile.pages.alert.Alert;
import com.tunein.mobile.pages.alert.IosAlert;
import com.tunein.mobile.pages.common.navigation.NavigationAction;
import com.tunein.mobile.pages.ios.navigation.IosNavigationAction;

public class IosToolBinds extends AbstractModule {

    protected void configure() {
        super.configure();
        bind(Alert.class).to(IosAlert.class).asEagerSingleton();
        bind(DeviceNativeActions.class).to(IosDeviceNativeActions.class).asEagerSingleton();
        bind(NavigationAction.class).to(IosNavigationAction.class).asEagerSingleton();
    }
}
