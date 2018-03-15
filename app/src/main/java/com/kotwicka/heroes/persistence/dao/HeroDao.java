package com.kotwicka.heroes.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.kotwicka.heroes.persistence.entity.Hero;

import java.util.List;

@Dao
public interface HeroDao {

    @Query("SELECT * FROM hero")
    List<Hero> getAll();

    @Query("SELECT * FROM hero WHERE name LIKE :name")
    List<Hero> getAllStartingWithName(final String name);

    @Insert
    void insert(Hero hero);

    @Insert
    void insertAll(Hero... hero);

    @Update
    void update(Hero hero);

}
