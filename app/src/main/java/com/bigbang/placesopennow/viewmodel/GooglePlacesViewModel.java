package com.bigbang.placesopennow.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;

import com.bigbang.placesopennow.database.FavoritePlacesDAO;
import com.bigbang.placesopennow.database.FavoritePlacesDB;
import com.bigbang.placesopennow.database.FavoritePlacesEntity;
import com.bigbang.placesopennow.model.LocationResultSet;
import com.bigbang.placesopennow.network.GooglePlacesRetrofitInstance;
import com.bigbang.placesopennow.util.Constants;
import com.bigbang.placesopennow.view.MainActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GooglePlacesViewModel extends AndroidViewModel {

    private GooglePlacesRetrofitInstance googlePlacesRetrofitInstance;
    private FavoritePlacesDB favoritePlacesDB;
    private FavoritePlacesDAO favoritePlacesDAO;

    public GooglePlacesViewModel(@NonNull Application application) {
        super(application);

        googlePlacesRetrofitInstance = new GooglePlacesRetrofitInstance();

        favoritePlacesDB = Room
                .databaseBuilder(application,
                        FavoritePlacesDB.class,
                        "favorite_places.db"
                )
                .allowMainThreadQueries()
                .build();

    }

    public Observable<LocationResultSet> getGooglePlacesData(String lat_long) {
        return  googlePlacesRetrofitInstance
                .getGooglePlacesData(lat_long, "1500", Constants.API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void addPlaceToFavorites(String placeIcon, String placeTitle) {
        favoritePlacesDB.getFavoritePlacesDAO().addFavoritePlace(new FavoritePlacesEntity(placeIcon, placeTitle));
    }

    public List<FavoritePlacesEntity> getFavoritePlaces() {
        return favoritePlacesDB.getFavoritePlacesDAO().selectAllFavoritePlaces();
    }
}
