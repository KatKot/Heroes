package com.kotwicka.heroes.detail.presenter;

import android.util.Log;

import com.google.common.base.Optional;
import com.kotwicka.heroes.detail.contract.HeroDetailContract;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.persistence.entity.Hero;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HeroDetailPresenter implements HeroDetailContract.Presenter {

    private final HeroDetailContract.Model model;
    private final HeroDetailContract.View view;
    protected Hero hero = null;

    public HeroDetailPresenter(final HeroDetailContract.Model model, final HeroDetailContract.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void checkFavouriteStatus(final HeroViewModel heroViewModel) {
        setFavouriteHeroData(heroViewModel);
    }

    @Override
    public void changeFavouriteStatus(final HeroViewModel heroViewModel) {
        if (hero == null) {
            addToFavourites(heroViewModel);
        } else {
            removeFromFavourites();
        }
        setFavouriteHeroData(heroViewModel);
    }

    private void removeFromFavourites() {
        model.removeFromFavourites(hero)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        view.displayDeletedMessage();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.displayErrorMessage();
                    }
                });
    }

    private void addToFavourites(final HeroViewModel heroViewModel) {
        model.addToFavourites(heroViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        view.displayAddedMessage();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.displayErrorMessage();
                    }
                });
    }

    private void setFavouriteHeroData(final HeroViewModel heroViewModel) {
        model.getFavourite(heroViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Optional<Hero>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Optional<Hero> hero) {
                        if (hero.isPresent()) {
                            HeroDetailPresenter.this.hero = hero.get();
                            view.setFavouriteButton();
                        } else {
                            HeroDetailPresenter.this.hero = null;
                            view.setNotFavouriteButton();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        view.displayErrorMessage();
                    }
                });
    }
}
