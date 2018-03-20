package com.kotwicka.heroes.list.contract;

import com.kotwicka.heroes.model.HeroViewModel;

import rx.Observable;


public interface HeroesContract {

    interface View {
        void updateData(HeroViewModel heroViewModel);
        void afterLoadingAllHeroes();
        void hideProgressItem();
        void showProgressItem();
    }

    interface Presenter {
        void loadHeroData();
        void loadFavouriteHeroes();
        void loadNextPageOfHeroData();
        void unsubscribeHeroData();
        void loadHeroData(String name);
        void resetApiModel();
        void resetApiOffset();
        boolean isShowingFavourites();
    }

    interface Model {
        Observable<HeroViewModel> heroes(int limit, int offset);
        Observable<HeroViewModel> heroesWithName(String name, int limit, int offset);
        Observable<HeroViewModel> favouriteHeroes(int limit, int offset);
    }
}
