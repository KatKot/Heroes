package com.kotwicka.heroes.presenter;

import android.util.Log;

import com.kotwicka.heroes.contract.HeroDetailContract;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.persistence.entity.Hero;

import rx.CompletableSubscriber;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HeroDetailPresenter implements HeroDetailContract.Presenter {

    private static final String TAG = HeroDetailPresenter.class.getSimpleName();

    private final HeroDetailContract.Model model;
    private final HeroDetailContract.View view;
    private Hero hero = null;

    public HeroDetailPresenter(final HeroDetailContract.Model model, final HeroDetailContract.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void checkFavouriteStatus(final HeroViewModel heroViewModel) {
        Log.d(TAG, "Checking initial favourite status...");
        setFavouriteHeroData(heroViewModel);
    }

    @Override
    public void changeFavouriteStatus(final HeroViewModel heroViewModel) {
        if (hero == null) {
            Log.d(TAG, "Hero not in fav, adding it");
            addToFavourites(heroViewModel);
        } else {
            Log.d(TAG, "Hero in fav, removing it");
            removeFromFavourites();
        }
        setFavouriteHeroData(heroViewModel);
    }

    private void removeFromFavourites() {
        model.removeFromFavourites(hero)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableSubscriber() {
                    @Override
                    public void onCompleted() {
                        view.displayDeletedMessage();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.displayErrorMessage();

                    }

                    @Override
                    public void onSubscribe(Subscription d) {

                    }
                });
    }

    private void addToFavourites(final HeroViewModel heroViewModel) {
        model.addToFavourites(heroViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableSubscriber() {
                    @Override
                    public void onCompleted() {
                        view.displayAddedMessage();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.displayErrorMessage();
                    }

                    @Override
                    public void onSubscribe(Subscription d) {

                    }
                });
    }

    private void setFavouriteHeroData(final HeroViewModel heroViewModel) {
        model.getFavourite(heroViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Hero>() {
                    @Override
                    public void onSuccess(Hero hero) {
                        HeroDetailPresenter.this.hero = hero;
                        if (hero != null) {
                            view.setFavouriteButton();
                        } else {
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
