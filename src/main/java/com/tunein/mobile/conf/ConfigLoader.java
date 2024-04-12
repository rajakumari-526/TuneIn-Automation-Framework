package com.tunein.mobile.conf;

import org.aeonbits.owner.ConfigFactory;

public class ConfigLoader {
        private static Configuration conf;

        public static Configuration config() {
            if (conf == null) {
                conf = ConfigFactory.create(Configuration.class, System.getProperties());
            }
            return conf;
        }
}
