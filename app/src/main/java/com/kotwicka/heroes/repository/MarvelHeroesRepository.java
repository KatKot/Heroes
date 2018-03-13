package com.kotwicka.heroes.repository;

import android.util.Log;

import com.kotwicka.heroes.net.api.MarvelService;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.net.model.Heroes;

import rx.Observable;
import rx.functions.Func1;

public class MarvelHeroesRepository implements HeroesRepository {

    private static final String TAG = MarvelHeroesRepository.class.getName();

    private MarvelService marvelService;

    public MarvelHeroesRepository(final MarvelService marvelService) {
        this.marvelService = marvelService;
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
}
