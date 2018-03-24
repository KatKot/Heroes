package com.kotwicka.heroes.detail.contract;

import com.google.common.base.Optional;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.persistence.entity.Hero;

import io.reactivex.Completable;
import io.reactivex.Single;

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
        Single<Optional<Hero>> getFavourite(HeroViewModel heroViewModel);
    }
}
