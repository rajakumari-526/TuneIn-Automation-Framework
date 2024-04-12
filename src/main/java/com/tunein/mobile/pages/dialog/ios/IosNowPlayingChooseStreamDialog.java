package com.tunein.mobile.pages.dialog.ios;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.dialog.common.NowPlayingChooseStreamDialog;
import com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static com.tunein.mobile.testdata.dataprovider.ContentProvider.StreamFormat.getStreamFormat;

public class IosNowPlayingChooseStreamDialog extends NowPlayingChooseStreamDialog {

    protected String streamFormatLocator = "(//*[contains(@name, '%s')])[1]";

    private String streamFormatAndBitrateLocator = "name CONTAINS '%s' AND name CONTAINS '%s'";

    @Step("Choose required stream format: {streamFormat}")
    @Override
    public NowPlayingPage chooseRequiredStreamFormat(StreamFormat streamFormat) {
        By requiredLocator = By.xpath(String.format(streamFormatLocator, streamFormat.getStreamFormat()));
        clickOnElement(requiredLocator);
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step
    @Override
    public NowPlayingPage chooseRequiredStreamFormat(StreamFormat streamFormat, StreamBitrate streamBitrate) {
        By requiredLocator = AppiumBy.iOSNsPredicateString(String.format(streamFormatAndBitrateLocator, streamFormat.name(), streamBitrate.getBitrateValue()));
        clickOnElement(requiredLocator);
        return nowPlayingPage.waitUntilPageReady();
    }

    @Override
    public List<StreamFormat> getStreamFormatList() {
        List<StreamFormat> list = new ArrayList<>();
        int size = streamTypes.size();
        for (int i = 0; i < size; i++) {
            String streamType = streamTypes.get(i).getText().split(" ")[1].trim();
            StreamFormat stream = getStreamFormat(streamType);
            if (!(list.contains(stream)) && stream != null) {
                list.add(stream);
            }
        }
        return list;
    }
}
