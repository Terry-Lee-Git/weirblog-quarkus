package com.weirblog.config;
import io.quarkus.jsonb.JsonbConfigCustomizer;
import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;

@Singleton
public class MyJsonbConfig implements JsonbConfigCustomizer {

    public void customize(JsonbConfig config) {
        config.withNullValues(true);
    }
}