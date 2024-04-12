package com.tunein.mobile.binds.asserts;

import com.google.inject.AbstractModule;
import com.tunein.mobile.reporting.asserts.common.UnifiedEventsAsserts;
import com.tunein.mobile.reporting.asserts.ios.IosUnifiedEventsAsserts;

public class IosAssertsBinds extends AbstractModule {

    protected void configure() {
        super.configure();
        bind(UnifiedEventsAsserts.class).to(IosUnifiedEventsAsserts.class).asEagerSingleton();
    }
}
