package com.bigbang.placesopennow.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// entities -> one for each table in db
@Database(version = 1, entities = { FavoritePlacesEntity.class })
public abstract class FavoritePlacesDB extends RoomDatabase {

    // one for every Entity needed
    public abstract FavoritePlacesDAO getFavoritePlacesDAO();
}
