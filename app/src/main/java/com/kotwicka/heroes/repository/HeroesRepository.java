package com.kotwicka.heroes.repository;

import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.persistence.entity.Hero;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface HeroesRepository {

    Observable<Data> getHeroes(int limit, int offset);

    Observable<Data> getHeroesWithName(String name, int limit, int offset);

    Single<Hero> getFavourite(HeroViewModel hero);

    Completable addHeroToFavourites(HeroViewModel hero);

    Completable removeHeroFromFavourites(Hero favouriteHero);
}
