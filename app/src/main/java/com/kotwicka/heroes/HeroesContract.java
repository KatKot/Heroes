package com.kotwicka.heroes;

import com.kotwicka.heroes.model.HeroViewModel;

import rx.Observable;


public interface HeroesContract {

    interface View {
        void updateData(HeroViewModel heroViewModel);
        void hideProgressBar();
    }

    interface Presenter {
        void loadHeroData();
        void unsubscribeHeroData();
    }

    interface Model {
        Observable<HeroViewModel> heroes(int limit, int offset);
    }
}
