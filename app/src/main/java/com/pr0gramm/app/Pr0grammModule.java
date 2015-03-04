package com.pr0gramm.app;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.pr0gramm.app.api.Api;
import com.pr0gramm.app.api.GifToWebmApi;
import com.pr0gramm.app.api.InstantDeserializer;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.joda.time.Instant;

import java.io.File;
import java.net.CookieHandler;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import roboguice.inject.SharedPreferencesName;

/**
 */
@SuppressWarnings("UnusedDeclaration")
public class Pr0grammModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CookieHandler.class).to(LoginCookieHandler.class);
    }

    @Provides
    @Singleton
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantDeserializer())
                .create();
    }

    @Provides
    @Singleton
    public Api api(Settings settings, Gson gson, CookieHandler cookieHandler) {
        OkHttpClient client = new OkHttpClient();

//        client.networkInterceptors().add(chain -> {
//            Request request = chain.request();
//
//            Crashlytics.log("Api: " + request.urlString());
//            return chain.proceed(request);
//        });

        client.setCookieHandler(cookieHandler);

        return new RestAdapter.Builder()
                .setEndpoint("http://pr0gramm.com")
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setClient(new OkClient(client))
                .build().create(Api.class);
    }

    @Provides
    @Singleton
    public GifToWebmApi restAdapter(Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(GifToWebmApi.ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build().create(GifToWebmApi.class);
    }

    @Provides
    @Singleton
    public Downloader downloader(Context context) {
        File cache = new File(context.getCacheDir(), "imgCache");
        return new OkHttpDownloader(cache);
    }

    @Provides
    @Singleton
    public Picasso picasso(Context context, Downloader downloader) {
        return new Picasso.Builder(context)
                .downloader(downloader)
                        // .loggingEnabled(true)
                        // .indicatorsEnabled(true)
                .build();
    }

    @Provides
    @SharedPreferencesName
    public String sharedPreferencesName() {
        return "pr0gramm";
    }
}
