package com.kotwicka.heroes.repository;

import android.util.Log;

import com.kotwicka.heroes.net.api.MarvelService;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.net.model.Heroes;
import com.kotwicka.heroes.persistence.database.HeroDatabase;

import rx.Observable;
import rx.functions.Func1;

public class MarvelHeroesRepository implements HeroesRepository {

    private static final String TAG = MarvelHeroesRepository.class.getName();

    private final MarvelService marvelService;
    private final HeroDatabase heroDatabase;

    public MarvelHeroesRepository(final MarvelService marvelService, final HeroDatabase heroDatabase) {
        this.marvelService = marvelService;
        this.heroDatabase = heroDatabase;
    }

    @Override
    public Observable<Data> getHeroes(final int limit, final int offset) {
        Log.d(TAG, "Getting heroes from net...");
        return marvelService.getHeroes(limit, offset).map(new Func1<Heroes, Data>() {
            @Override
            public Data call(Heroes heroes) {
                return heroes.getData();
            }
        });
    }

    @Override
    public Observable<Data> getHeroesWithName(final String name, final int limit, final int offset) {
        Log.d(TAG, "Getting heroes for name : " + name + " ...");
        return marvelService.getHeroesWithNameStartingWith(name, limit, offset).map(new Func1<Heroes, Data>() {
            @Override
            public Data call(Heroes heroes) {
                return heroes.getData();
            }
        });
    }
}
