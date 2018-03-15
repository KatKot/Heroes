package com.kotwicka.heroes.persistence;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kotwicka.heroes.persistence.database.HeroDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@Module
public class PersistenceModule {

    public static final String HERO_DB_NAME = "hero";

    @Provides
    @Singleton
    public HeroDatabase providesHeroDatabase(final Context applicationContext) {
        return Room.databaseBuilder(applicationContext, HeroDatabase.class, HERO_DB_NAME).build();
    }
}
