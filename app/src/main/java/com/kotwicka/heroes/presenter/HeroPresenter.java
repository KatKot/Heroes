package com.kotwicka.heroes.presenter;

import android.util.Log;

import com.kotwicka.heroes.HeroesContract;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.utils.HeroApiParameters;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HeroPresenter implements HeroesContract.Presenter {

    private static final String TAG = HeroPresenter.class.getName();

    private final HeroesContract.Model heroModel;
    private final HeroesContract.View heroView;

    private Subscription subscription = null;

    public HeroPresenter(final HeroesContract.Model model, final HeroesContract.View view) {
        this.heroModel = model;
        this.heroView = view;
    }

    @Override
    public void loadHeroData() {
        subscription = getHeroes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HeroViewModel>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Completed!");
                        heroView.hideProgressBar();
                        heroView.showProgressItem();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Error " + e.getMessage());
                    }

                    @Override
                    public void onNext(HeroViewModel heroViewModel) {
                        HeroApiParameters.TOTAL = heroViewModel.getTotal();
                        heroView.updateData(heroViewModel);
                        Log.d(TAG, "Updating data with new model...");
                    }
                });
    }

    @Override
    public void loadNextPageOfHeroData() {
        subscription = getHeroes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HeroViewModel>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Completed!");
                        heroView.showProgressItem();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Error " + e.getMessage());
                    }

                    @Override
                    public void onNext(HeroViewModel heroViewModel) {
                        heroView.hideProgressItem();
                        heroView.updateData(heroViewModel);
                        Log.d(TAG, "Updating data with new model...");
                    }
                });
    }

    private Observable<HeroViewModel> getHeroes() {
        if (HeroApiParameters.GET_FAVOURITES) {
            return heroModel.favouriteHeroes(HeroApiParameters.LIMIT, HeroApiParameters.OFFSET);
        } else if (HeroApiParameters.NAME.isEmpty()) {
            return heroModel.heroes(HeroApiParameters.LIMIT, HeroApiParameters.OFFSET);
        } else {
            return heroModel.heroesWithName(HeroApiParameters.NAME, HeroApiParameters.LIMIT, HeroApiParameters.OFFSET);
        }
    }

    @Override
    public void unsubscribeHeroData() {
        if (this.subscription != null && !this.subscription.isUnsubscribed()) {
            this.subscription.unsubscribe();
        }
    }
}
