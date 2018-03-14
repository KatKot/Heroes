package com.kotwicka.heroes.repository;

import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.net.model.Heroes;
import com.kotwicka.heroes.net.model.Result;

import rx.Observable;

public interface HeroesRepository {

    Observable<Data> getHeroes(int limit, int offset);

    Observable<Data> getHeroesWithName(String name, int limit, int offset);
}
