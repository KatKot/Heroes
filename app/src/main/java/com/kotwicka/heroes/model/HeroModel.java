package com.kotwicka.heroes.model;

import android.util.Log;

import com.kotwicka.heroes.HeroesContract;
import com.kotwicka.heroes.contract.HeroDetailContract;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.net.model.Heroes;
import com.kotwicka.heroes.net.model.Result;
import com.kotwicka.heroes.persistence.entity.Hero;
import com.kotwicka.heroes.presenter.HeroPresenter;
import com.kotwicka.heroes.repository.HeroesRepository;
import com.kotwicka.heroes.utils.HeroApiParameters;

import java.util.Date;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;

public class HeroModel implements HeroesContract.Model, HeroDetailContract.Model {

    private static final String TAG = HeroModel.class.getName();

    private final HeroesRepository repository;

    public HeroModel(final HeroesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<HeroViewModel> heroes(final int limit, final int offset) {
        Log.d(TAG, "Mapping heroes to view model...");
        return repository.getHeroes(limit, offset).concatMap(new Func1<Data, Observable<? extends HeroViewModel>>() {
            @Override
            public Observable<? extends HeroViewModel> call(final Data data) {
                Log.d(TAG, "Count: " + data.getCount() + " , Total : " + data.getTotal()
                        + " , Offset: " + data.getOffset() + " , Limit: " + data.getLimit());
                return Observable.from(data.getResults()).map(new Func1<Result, HeroViewModel>() {
                    @Override
                    public HeroViewModel call(Result result) {
                        return new HeroViewModel(result, data.getTotal());
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
                Log.d(TAG, "Name : " + name + " Count: " + data.getCount() + " , Total : " + data.getTotal()
                        + " , Offset: " + data.getOffset() + " , Limit: " + data.getLimit());
                return Observable.from(data.getResults()).map(new Func1<Result, HeroViewModel>() {
                    @Override
                    public HeroViewModel call(Result result) {
                        return new HeroViewModel(result, data.getTotal());
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
