package com.tunein.mobile.api.testrail;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.tunein.mobile.conf.ConfigLoader.config;

public class TestRailOkClient extends OkHttpClient {

    public static OkHttpClient getTestRailOkClient() {
        Builder httpClient = new Builder()
                .connectTimeout(config().fiveMinutesInSeconds(), TimeUnit.SECONDS)
                .readTimeout(config().fiveMinutesInSeconds(), TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));

        httpClient
                .authenticator(new Authenticator() {
                    @Nullable
                    @Override
                    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
                        String username = "web.automation@tunien.com";
                        String token = config().testRailAuthorizationToken();
                        String credential = Credentials.basic(username, token);
                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();
                    }
                })
                .addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .header("Content-Type", "application/json")
                    .build();
            return chain.proceed(request);
        });

        httpClient.addInterceptor(chain -> {
            String url = chain.request().url().toString().replaceAll("%3F", "?");
            return chain.proceed(
                    chain.request().newBuilder()
                            .url(url)
                            .build());
        });

        return httpClient.build();
    }
}
