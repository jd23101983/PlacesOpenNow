package com.bigbang.placesopennow.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "FavoritePlaces")
public class FavoritePlacesEntity {

    @PrimaryKey(autoGenerate = true)
    private int placeID;

    @ColumnInfo(name = "placeIcon")
    private String placeIcon;

    @ColumnInfo(name = "placeName")
    private String placeName;

    public FavoritePlacesEntity(int placeID, String placeIcon, String placeName) {
        this.placeID = placeID;
        this.placeIcon = placeIcon;
        this.placeName = placeName;
    }

    @Ignore
    public FavoritePlacesEntity(String placeIcon, String placeName) {
        this.placeIcon = placeIcon;
        this.placeName = placeName;
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public String getPlaceIcon() {
        return placeIcon;
    }

    public void setPlaceIcon(String placeIcon) {
        this.placeIcon = placeIcon;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
