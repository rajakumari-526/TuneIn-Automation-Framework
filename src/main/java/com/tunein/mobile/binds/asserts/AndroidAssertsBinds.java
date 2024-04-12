package com.tunein.mobile.binds.asserts;

import com.google.inject.AbstractModule;
import com.tunein.mobile.reporting.asserts.android.AndroidUnifiedEventsAsserts;
import com.tunein.mobile.reporting.asserts.common.UnifiedEventsAsserts;

public class AndroidAssertsBinds extends AbstractModule {

    protected void configure() {
        super.configure();
        bind(UnifiedEventsAsserts.class).to(AndroidUnifiedEventsAsserts.class).asEagerSingleton();
    }
}
