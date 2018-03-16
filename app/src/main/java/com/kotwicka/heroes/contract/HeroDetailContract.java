package com.kotwicka.heroes.contract;

import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.persistence.entity.Hero;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface HeroDetailContract {

    interface View {
        void displayAddedMessage();
        void displayDeletedMessage();
        void displayErrorMessage();
        void setFavouriteButton();
        void setNotFavouriteButton();
    }

    interface Presenter {
        void checkFavouriteStatus(HeroViewModel heroViewModel);
        void changeFavouriteStatus(HeroViewModel heroViewModel);
    }

    interface Model {
        Completable addToFavourites(HeroViewModel heroViewModel);
        Completable removeFromFavourites(Hero hero);
        Single<Hero> getFavourite(HeroViewModel heroViewModel);
    }
}
