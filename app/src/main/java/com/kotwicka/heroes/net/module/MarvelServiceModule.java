package com.kotwicka.heroes.net.module;

import android.content.Context;

import com.kotwicka.heroes.R;
import com.kotwicka.heroes.model.HeroApiModel;
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
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MarvelServiceModule {

    private static final String MARVEL_SERVICE_BASE_URL = "https://gateway.marvel.com:443";
    private static final String API_PARAMETER_KEY = "apikey";
    private static final String TIMESTAMP_PARAMETER_KEY = "ts";
    private static final String HASH_PARAMETER_KEY = "hash";


    @Provides
    @Singleton
    public GsonConverterFactory providesConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    public RxJava2CallAdapterFactory providesCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    public Interceptor providesInterceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String timestamp = String.valueOf(new Date().getTime());
                String baseForHash = timestamp + context.getString(R.string.marvel_api_private_key) + context.getString(R.string.marvel_api_key);
                String hashValue = new String(Hex.encodeHex(DigestUtils.md5(baseForHash)));
                HttpUrl url = chain.request().url().newBuilder()
                        .addQueryParameter(TIMESTAMP_PARAMETER_KEY, timestamp)
                        .addQueryParameter(API_PARAMETER_KEY, context.getString(R.string.marvel_api_key))
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
            final RxJava2CallAdapterFactory rxJavaCallAdapterFactory,
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

    @Singleton
    @Provides
    public HeroApiModel providesHeroApiModel() {
        return new HeroApiModel();
    }
}
