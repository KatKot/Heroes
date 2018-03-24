package com.kotwicka.heroes.model;

import com.google.common.base.Optional;
import com.kotwicka.heroes.detail.contract.HeroDetailContract;
import com.kotwicka.heroes.list.contract.HeroesContract;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.net.model.Result;
import com.kotwicka.heroes.persistence.entity.Hero;
import com.kotwicka.heroes.repository.HeroesRepository;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class HeroModel implements HeroesContract.Model, HeroDetailContract.Model {

    private final HeroesRepository repository;
    private final HeroApiModel heroApiModel;

    public HeroModel(final HeroesRepository repository, final HeroApiModel heroApiModel) {
        this.repository = repository;
        this.heroApiModel = heroApiModel;
    }

    @Override
    public Observable<HeroViewModel> heroes(final int limit, final int offset) {
        return getHeroes(repository.getHeroes(limit, offset));
    }

    @Override
    public Observable<HeroViewModel> heroesWithName(final String name, final int limit, final int offset) {
        return getHeroes(repository.getHeroesWithName(name, limit, offset));
    }

    private Observable<HeroViewModel> getHeroes(final Observable<Data> data) {
        return data.concatMap(new Function<Data, ObservableSource<? extends HeroViewModel>>() {
            @Override
            public ObservableSource<? extends HeroViewModel> apply(Data data) throws Exception {
                heroApiModel.setTotal(Integer.valueOf(data.getTotal()));
                return Observable.fromArray(data.getResultsArray()).map(new Function<Result, HeroViewModel>() {
                    @Override
                    public HeroViewModel apply(Result result) throws Exception {
                        return new HeroViewModel(result);
                    }
                });
            }
        });
    }


    @Override
    public Observable<HeroViewModel> favouriteHeroes(final int limit, final int offset) {
        return repository.getFavouriteHeroes(limit, offset).concatMap(new Function<FavouriteHeroes, ObservableSource<? extends HeroViewModel>>() {
            @Override
            public ObservableSource<? extends HeroViewModel> apply(final FavouriteHeroes favouriteHeroes) {
                heroApiModel.setTotal(Integer.valueOf(favouriteHeroes.getTotalSize()));
                return Observable.fromArray(favouriteHeroes.getFavouriteHeroesPage()).map(new Function<Hero, HeroViewModel>() {
                    @Override
                    public HeroViewModel apply(Hero hero) {
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
    public Single<Optional<Hero>> getFavourite(final HeroViewModel heroViewModel) {
        return repository.getFavourite(heroViewModel);
    }
}
