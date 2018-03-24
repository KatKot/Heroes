package com.kotwicka.heroes.list.presenter;

import android.util.Log;

import com.kotwicka.heroes.list.contract.HeroesContract;
import com.kotwicka.heroes.model.HeroApiModel;
import com.kotwicka.heroes.model.HeroViewModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HeroPresenter implements HeroesContract.Presenter {

    private static final String TAG = HeroPresenter.class.getName();

    private final HeroesContract.Model heroModel;
    private final HeroesContract.View heroView;
    private final HeroApiModel heroApiModel;

    private Disposable disposable;

    public HeroPresenter(final HeroesContract.Model model, final HeroesContract.View view, final HeroApiModel heroApiModel) {
        this.heroModel = model;
        this.heroView = view;
        this.heroApiModel = heroApiModel;
    }

    @Override
    public void loadHeroData() {
            getHeroes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HeroViewModel>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error loading heroes " + e.getMessage(), e);
                        heroView.onErrorFetchingData();

                    }

                    @Override
                    public void onComplete() {
                        heroView.afterLoadingAllHeroes();
                        showProgressItem();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
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
        getHeroes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HeroViewModel>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error loading heroes " + e.getMessage(), e);
                        heroView.onErrorFetchingData();
                        heroView.hideProgressItem();
                    }

                    @Override
                    public void onComplete() {
                        showProgressItem();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
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
        if (this.disposable != null && !this.disposable.isDisposed()) {
            this.disposable.dispose();
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
