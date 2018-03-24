package com.kotwicka.heroes.repository;

import com.google.common.base.Optional;
import com.kotwicka.heroes.model.FavouriteHeroes;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.net.api.MarvelService;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.net.model.Heroes;
import com.kotwicka.heroes.persistence.database.HeroDatabase;
import com.kotwicka.heroes.persistence.entity.Hero;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class MarvelHeroesRepository implements HeroesRepository {

    private final MarvelService marvelService;
    private final HeroDatabase heroDatabase;

    public MarvelHeroesRepository(final MarvelService marvelService, final HeroDatabase heroDatabase) {
        this.marvelService = marvelService;
        this.heroDatabase = heroDatabase;
    }

    @Override
    public Observable<Data> getHeroes(final int limit, final int offset) {
        return mapHeroes(marvelService.getHeroes(limit, offset));
    }

    @Override
    public Observable<Data> getHeroesWithName(final String name, final int limit, final int offset) {
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
        return Observable.zip(favourites, favouriteSize, new BiFunction<List<Hero>, Integer, FavouriteHeroes>() {
            @Override
            public FavouriteHeroes apply(List<Hero> heroes, Integer integer) throws Exception {
                return new FavouriteHeroes(heroes, integer);
            }
        });
    }


    @Override
    public Single<Optional<Hero>> getFavourite(final HeroViewModel hero) {
        return Single.fromCallable(new Callable<Optional<Hero>>() {
            @Override
            public Optional<Hero> call() throws Exception {
                return Optional.fromNullable(heroDatabase.heroDao().getForName(hero.getName()));
            }
        });
    }

    @Override
    public Completable addHeroToFavourites(final HeroViewModel hero) {
        final Hero favouriteHero = new Hero();
        favouriteHero.setName(hero.getName());
        favouriteHero.setDescription(hero.getDescription());
        favouriteHero.setPhotoPath(hero.getPhotoPath());

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                heroDatabase.heroDao().insert(favouriteHero);
            }
        });
    }

    @Override
    public Completable removeHeroFromFavourites(final Hero favouriteHero) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                heroDatabase.heroDao().delete(favouriteHero);
            }
        });
    }

    private Observable<Data> mapHeroes(final Observable<Heroes> heroes) {
        return heroes.map(new Function<Heroes, Data>() {
            @Override
            public Data apply(Heroes heroes) throws Exception {
                return heroes.getData();
            }
        });
    }
}
