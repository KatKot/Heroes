package com.kotwicka.heroes.model;

import com.kotwicka.heroes.list.contract.HeroesContract;
import com.kotwicka.heroes.detail.contract.HeroDetailContract;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.net.model.Result;
import com.kotwicka.heroes.persistence.entity.Hero;
import com.kotwicka.heroes.repository.HeroesRepository;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;

public class HeroModel implements HeroesContract.Model, HeroDetailContract.Model {

    private final HeroesRepository repository;
    private final HeroApiModel heroApiModel;

    public HeroModel(final HeroesRepository repository, final HeroApiModel heroApiModel) {
        this.repository = repository;
        this.heroApiModel = heroApiModel;
    }

    @Override
    public Observable<HeroViewModel> heroes(final int limit, final int offset) {
        return repository.getHeroes(limit, offset).concatMap(new Func1<Data, Observable<? extends HeroViewModel>>() {
            @Override
            public Observable<? extends HeroViewModel> call(final Data data) {
                heroApiModel.setTotal(Integer.valueOf(data.getTotal()));
                return Observable.from(data.getResults()).map(new Func1<Result, HeroViewModel>() {
                    @Override
                    public HeroViewModel call(Result result) {
                        return new HeroViewModel(result);
                    }
                });
            }
        });
    }

    @Override
    public Observable<HeroViewModel> heroesWithName(final String name, final int limit, final int offset) {
        return repository.getHeroesWithName(name, limit, offset).concatMap(new Func1<Data, Observable<? extends HeroViewModel>>() {
            @Override
            public Observable<? extends HeroViewModel> call(final Data data) {
                heroApiModel.setTotal(Integer.valueOf(data.getTotal()));
                return Observable.from(data.getResults()).map(new Func1<Result, HeroViewModel>() {
                    @Override
                    public HeroViewModel call(Result result) {
                        return new HeroViewModel(result);
                    }
                });
            }
        });
    }

    @Override
    public Observable<HeroViewModel> favouriteHeroes(final int limit, final int offset) {
        return repository.getFavouriteHeroes(limit, offset).concatMap(new Func1<FavouriteHeroes, Observable<? extends HeroViewModel>>() {
            @Override
            public Observable<? extends HeroViewModel> call(final FavouriteHeroes favouriteHeroes) {
                heroApiModel.setTotal(Integer.valueOf(favouriteHeroes.getTotalSize()));
                return Observable.from(favouriteHeroes.getFavouriteHeroesPage()).map(new Func1<Hero, HeroViewModel>() {
                    @Override
                    public HeroViewModel call(Hero hero) {
                        return new HeroViewModel(hero);
                    }
                });
            }
        });
    }

    @Override
    public Completable addToFavourites(final HeroViewModel heroViewModel) {
        return repository.addHeroToFavourites(heroViewModel);
    }

    @Override
    public Completable removeFromFavourites(final Hero hero) {
        return repository.removeHeroFromFavourites(hero);
    }

    @Override
    public Single<Hero> getFavourite(final HeroViewModel heroViewModel) {
        return repository.getFavourite(heroViewModel);
    }
}
