package com.tunein.mobile.testdata.dataprovider;

import com.google.gson.*;
import com.tunein.mobile.testdata.models.HeadspinStreams;
import com.tunein.mobile.utils.ReporterUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.tunein.mobile.conf.ConfigLoader.config;

public class HeadspinProvider {

    /**
     * Defaults to "streams/Streams.json" if "appiumStreamLocalStreamsJsonPath()" not set
     * Path to the local Streams.json on Mac is "/Users/serviceaccount/Streams.json"
     */
    private static final String FILE_WITH_CONTENTS = config().appiumStreamLocalStreamsJsonPath();

    private static final String DEFAULT_STREAMS_JSON_PATH = "streams/Streams.json";

    private static final String LOCAL = "Local";

    private static final String DEFAULT = "Default";

    private static final String FILE_TYPE = (FILE_WITH_CONTENTS.equals(DEFAULT_STREAMS_JSON_PATH)) ? DEFAULT : LOCAL;

    public static HeadspinStreams getContent(String udid) {
        ReporterUtil.log(FILE_TYPE + " " + FILE_WITH_CONTENTS + " is in use");

        HeadspinStreams headspinStreams = new HeadspinStreams();
        Gson gson = new GsonBuilder().create();
        try {
            File jsonFile = new File(FILE_TYPE.equals(DEFAULT)
                    ? UserProvider.class.getClassLoader().getResource(FILE_WITH_CONTENTS).getFile() // location of the fileName is the root folder.
                    : FILE_WITH_CONTENTS // location of the file provided by config key that is located outside the root folder.
            );

            FileReader reader = new FileReader(jsonFile.getAbsolutePath());
            String toString = IOUtils.toString(reader);
            JsonArray jsonArray = JsonParser.parseString(toString).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                if (object.has(udid)) {
                    headspinStreams = gson.fromJson(object.get(udid), HeadspinStreams.class);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return headspinStreams;
    }

}
