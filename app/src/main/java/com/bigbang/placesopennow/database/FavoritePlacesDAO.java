package com.bigbang.placesopennow.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritePlacesDAO {

    @Insert
    void addFavoritePlace(FavoritePlacesEntity favoritePlace);

    @Delete
    void deleteFavoritePlace(FavoritePlacesEntity favoritePlace);

    @Query("SELECT * FROM FavoritePlaces")
    List<FavoritePlacesEntity> selectAllFavoritePlaces();
}
