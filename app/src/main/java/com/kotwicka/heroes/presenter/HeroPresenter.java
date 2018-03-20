package com.kotwicka.heroes.presenter;

import android.util.Log;

import com.kotwicka.heroes.contract.HeroesContract;
import com.kotwicka.heroes.model.HeroApiModel;
import com.kotwicka.heroes.model.HeroViewModel;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HeroPresenter implements HeroesContract.Presenter {

    private static final String TAG = HeroPresenter.class.getName();

    private final HeroesContract.Model heroModel;
    private final HeroesContract.View heroView;
    private final HeroApiModel heroApiModel;
    private Subscription subscription = null;

    public HeroPresenter(final HeroesContract.Model model, final HeroesContract.View view, final HeroApiModel heroApiModel) {
        this.heroModel = model;
        this.heroView = view;
        this.heroApiModel = heroApiModel;
    }

    @Override
    public void loadHeroData() {
        subscription = getHeroes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HeroViewModel>() {
                    @Override
                    public void onCompleted() {
                        heroView.afterLoadingAllHeroes();
                        showProgressItem();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(HeroViewModel heroViewModel) {
                        heroView.updateData(heroViewModel);
                    }
                });
    }

    private void showProgressItem() {
        if (!heroApiModel.isLastPage()) {
            heroView.showProgressItem();
        }
    }

    @Override
    public void loadFavouriteHeroes() {
        heroApiModel.setShouldGetFavourites(true);
        loadHeroData();
    }

    @Override
    public void loadNextPageOfHeroData() {
        subscription = getHeroes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HeroViewModel>() {
                    @Override
                    public void onCompleted() {
                        showProgressItem();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(HeroViewModel heroViewModel) {
                        heroView.hideProgressItem();
                        heroView.updateData(heroViewModel);
                    }
                });
    }

    private Observable<HeroViewModel> getHeroes() {
        final int limit = heroApiModel.getLimit();
        final int offset = heroApiModel.getOffset();
        if (heroApiModel.shouldGetFavourites()) {
            return heroModel.favouriteHeroes(limit, offset);
        } else if (heroApiModel.getName().isEmpty()) {
            return heroModel.heroes(limit, offset);
        } else {
            return heroModel.heroesWithName(heroApiModel.getName(), limit, offset);
        }
    }

    @Override
    public void unsubscribeHeroData() {
        if (this.subscription != null && !this.subscription.isUnsubscribed()) {
            this.subscription.unsubscribe();
        }
    }

    @Override
    public void loadHeroData(final String name) {
        heroApiModel.setName(name);
        loadHeroData();
    }

    @Override
    public void resetApiModel() {
        heroApiModel.resetParameters();
    }

    @Override
    public void resetApiOffset() {
        heroApiModel.resetOffset();
    }

    @Override
    public boolean isShowingFavourites() {
        return heroApiModel.shouldGetFavourites();
    }
}
