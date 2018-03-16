package com.kotwicka.heroes.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.kotwicka.heroes.persistence.entity.Hero;

@Dao
public interface HeroDao {

    @Query("SELECT * FROM hero WHERE name = :name")
    Hero getForName(final String name);

    @Insert
    void insert(Hero hero);

    @Delete
    void delete(Hero hero);

}
