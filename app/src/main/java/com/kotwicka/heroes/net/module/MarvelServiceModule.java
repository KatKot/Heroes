package com.kotwicka.heroes.net.module;

import com.kotwicka.heroes.net.api.MarvelService;
import com.kotwicka.heroes.persistence.database.HeroDatabase;
import com.kotwicka.heroes.repository.HeroesRepository;
import com.kotwicka.heroes.repository.MarvelHeroesRepository;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MarvelServiceModule {

    private static final String MARVEL_SERVICE_BASE_URL = "https://gateway.marvel.com:443";
    private static final String API_PARAMETER_KEY = "apikey";
    private static final String TIMESTAMP_PARAMETER_KEY = "ts";
    private static final String HASH_PARAMETER_KEY = "hash";
    private static final String API_KEY_VALUE = "d8c01c8c7c0ce28dad420f1cbfb5d2bb";
    private static final String PRIVATE_API_KEY_VALUE = "fe9a145da2d3742933e850fed0a707b0f3229fa2";


    @Provides
    @Singleton
    public GsonConverterFactory providesConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    public RxJavaCallAdapterFactory providesCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }

    @Provides
    @Singleton
    public Interceptor providesInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String timestamp = String.valueOf(new Date().getTime());
                String baseForHash = timestamp + PRIVATE_API_KEY_VALUE + API_KEY_VALUE;
                String hashValue = new String(Hex.encodeHex(DigestUtils.md5(baseForHash)));
                HttpUrl url = chain.request().url().newBuilder()
                        .addQueryParameter(TIMESTAMP_PARAMETER_KEY, timestamp)
                        .addQueryParameter(API_PARAMETER_KEY, API_KEY_VALUE)
                        .addQueryParameter(HASH_PARAMETER_KEY, hashValue)
                        .build();
                return chain.proceed(chain.request().newBuilder()
                        .url(url)
                        .build());
            }
        };
    }

    @Provides
    @Singleton
    public OkHttpClient providesClient(final Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Singleton
    @Provides
    public Retrofit providesRetrofit(
            final GsonConverterFactory gsonConverterFactory,
            final RxJavaCallAdapterFactory rxJavaCallAdapterFactory,
            final OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(MARVEL_SERVICE_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }

    @Singleton
    @Provides
    public MarvelService providesMarvelService(Retrofit retrofit) {
        return retrofit.create(MarvelService.class);
    }

    @Singleton
    @Provides
    public HeroesRepository providesHeroesRepository(final MarvelService marvelService, final HeroDatabase heroDatabase) {
        return new MarvelHeroesRepository(marvelService, heroDatabase);
    }
}
