package com.kotwicka.heroes.model;

import com.kotwicka.heroes.persistence.entity.Hero;

import java.util.List;

public class FavouriteHeroes {

    private final List<Hero> favouriteHeroesPage;
    private final int totalSize;

    public FavouriteHeroes(final List<Hero> favouriteHeroesPage, final int totalSize) {
        this.favouriteHeroesPage = favouriteHeroesPage;
        this.totalSize = totalSize;
    }

    public Hero[] getFavouriteHeroesPage() {
        return favouriteHeroesPage.toArray(new Hero[favouriteHeroesPage.size()]);
    }

    public int getTotalSize() {
        return totalSize;
    }
}
