package com.kotwicka.heroes.repository;

import android.util.Log;

import com.kotwicka.heroes.model.FavouriteHeroes;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.net.api.MarvelService;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.net.model.Heroes;
import com.kotwicka.heroes.persistence.database.HeroDatabase;
import com.kotwicka.heroes.persistence.entity.Hero;

import java.util.List;
import java.util.concurrent.Callable;

import butterknife.OnCheckedChanged;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;

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
        return mapHeroes(marvelService.getHeroes(limit, offset));
    }

    @Override
    public Observable<Data> getHeroesWithName(final String name, final int limit, final int offset) {
        Log.d(TAG, "Getting heroes for name : " + name + " ...");
        return mapHeroes(marvelService.getHeroesWithNameStartingWith(name, limit, offset));
    }

    @Override
    public Observable<FavouriteHeroes> getFavouriteHeroes(final int limit, final int offset) {
       final Observable<List<Hero>> favourites = Observable.fromCallable(new Callable<List<Hero>>() {
            @Override
            public List<Hero> call() throws Exception {
                return heroDatabase.heroDao().getHeroes(limit, offset);
            }
        });
        final Observable<Integer> favouriteSize = Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return heroDatabase.heroDao().getHeroesSize();
            }
        });
        return Observable.zip(favourites, favouriteSize, new Func2<List<Hero>, Integer, FavouriteHeroes>() {
            @Override
            public FavouriteHeroes call(List<Hero> heroes, Integer totalSize) {
                return new FavouriteHeroes(heroes, totalSize);
            }
        });
    }

    @Override
    public Single<Hero> getFavourite(final HeroViewModel hero) {
        return Single.fromCallable(new Callable<Hero>() {
            @Override
            public Hero call() throws Exception {
                return heroDatabase.heroDao().getForName(hero.getName());
            }
        });
    }

    @Override
    public Completable addHeroToFavourites(final HeroViewModel hero) {
        final Hero favouriteHero = new Hero();
        favouriteHero.setName(hero.getName());
        favouriteHero.setDescription(hero.getDescription());
        favouriteHero.setPhotoPath(hero.getPhotoPath());

        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                heroDatabase.heroDao().insert(favouriteHero);
            }
        });
    }

    @Override
    public Completable removeHeroFromFavourites(final Hero favouriteHero) {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                heroDatabase.heroDao().delete(favouriteHero);
            }
        });
    }

    private Observable<Data> mapHeroes(final Observable<Heroes> heroes) {
        return heroes.map(new Func1<Heroes, Data>() {
            @Override
            public Data call(Heroes heroes) {
                return heroes.getData();
            }
        });
    }
}
