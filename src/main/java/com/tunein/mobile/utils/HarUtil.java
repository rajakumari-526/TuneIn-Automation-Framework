package com.tunein.mobile.utils;

import com.epam.reportportal.annotations.Step;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.model.*;

import java.io.File;
import java.util.List;

public class HarUtil {

    @Step("Get all har entries with domwain {domainUrl}")
    public List<HarEntry> getHarEntriesWithDomain(String harFilePath, String domainUrl) {
        File harFile = new File(harFilePath);

        // Create a HarReader
        HarReader harReader = new HarReader();

        try {
            // Read the HAR file
            Har har = harReader.readFromFile(harFile);

            // Get a list of all entries
            List<HarEntry> allEntries = har.getLog().getEntries();

            // Filter requests by the specified domain URL
            List<HarEntry> filteredEntries = allEntries.stream()
                    .filter(entry -> entry.getRequest().getUrl().contains(domainUrl))
                    .toList();

            return filteredEntries;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
