package com.kotwicka.heroes.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.kotwicka.heroes.persistence.entity.Hero;

import java.util.List;

@Dao
public interface HeroDao {

    @Query("SELECT * FROM hero WHERE name = :name")
    Hero getForName(final String name);

    @Query("SELECT * FROM hero ORDER BY name ASC LIMIT :limit OFFSET :offset")
    List<Hero> getHeroes(final int limit, final int offset);

    @Query("SELECT COUNT(*) FROM hero")
    int getHeroesSize();

    @Insert
    void insert(Hero hero);

    @Delete
    void delete(Hero hero);

}
