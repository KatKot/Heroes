package com.kotwicka.heroes.persistence.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.kotwicka.heroes.persistence.dao.HeroDao;
import com.kotwicka.heroes.persistence.entity.Hero;

@Database(entities = {Hero.class}, version = 1)
public abstract class HeroDatabase extends RoomDatabase {

    public abstract HeroDao heroDao();
}
