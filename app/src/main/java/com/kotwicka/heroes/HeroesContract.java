package com.kotwicka.heroes;

import com.kotwicka.heroes.model.HeroViewModel;

import rx.Observable;


public interface HeroesContract {

    interface View {
        void updateData(HeroViewModel heroViewModel);
        void hideProgressBar();
        void hideProgressItem();
        void showProgressItem();
    }

    interface Presenter {
        void loadHeroData();
        void loadNextPageOfHeroData();
        void unsubscribeHeroData();
    }

    interface Model {
        Observable<HeroViewModel> heroes(int limit, int offset);
        Observable<HeroViewModel> heroesWithName(String name, int limit, int offset);
        Observable<HeroViewModel> favouriteHeroes(int limit, int offset);
    }
}
