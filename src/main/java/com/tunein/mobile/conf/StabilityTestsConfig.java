package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface StabilityTestsConfig extends Config {

    @Key("stability.tests")
    @DefaultValue("false")
    boolean stabilityTests();

    @Key("dev.pr")
    @DefaultValue("false")
    boolean isDevPR();

    @Key("write.to.file.stability.results")
    @DefaultValue("false")
    boolean writeToFileStabilityResults();

    @Key("stability.tests.master.file.name")
    @DefaultValue("stability_test_results_master.csv")
    String stabilityTestsMasterFileName();

    @Key("memory.monitoring.tests.master.file.name")
    @DefaultValue("memory_values_master.csv")
    String memoryMonitoringTestsMasterFileName();

    @Key("stability.tests.master.file.path")
    @DefaultValue("/Users/serviceaccount/Downloads/Headspin_Files/master_csv/")
    String stabilityTestsMasterFilePath();

    @Key("stability.tests.report.file.path")
    @DefaultValue("/Users/serviceaccount/Downloads/Headspin_Files/")
    String stabilityTestsReportFilePath();

    @Key("stability.tests.report.file.name")
    @DefaultValue("stability_test_source_results")
    String stabilityTestsReportFileName();

    @Key("har.file.folder")
    @DefaultValue("/Users/serviceaccount/dev/har-files/")
    String headSpinHarFolder();

    @Key("critical.memory.value.bytes")
    @DefaultValue("800000")
    int criticalMemoryValueBytes();

}
