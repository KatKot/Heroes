package com.kotwicka.heroes.repository;

import com.google.common.base.Optional;
import com.kotwicka.heroes.model.FavouriteHeroes;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.persistence.entity.Hero;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public interface HeroesRepository {

    Observable<Data> getHeroes(int limit, int offset);

    Observable<Data> getHeroesWithName(String name, int limit, int offset);

    Observable<FavouriteHeroes> getFavouriteHeroes(final int limit, final int offset);

    Single<Optional<Hero>> getFavourite(HeroViewModel hero);

    Completable addHeroToFavourites(HeroViewModel hero);

    Completable removeHeroFromFavourites(Hero favouriteHero);
}
