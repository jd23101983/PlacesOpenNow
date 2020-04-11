package com.bigbang.placesopennow.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.bigbang.placesopennow.model.LocationResultSet;
import com.bigbang.placesopennow.network.GooglePlacesRetrofitInstance;
import com.bigbang.placesopennow.util.Constants;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GooglePlacesViewModel extends AndroidViewModel {

    private GooglePlacesRetrofitInstance googlePlacesRetrofitInstance;

    public GooglePlacesViewModel(@NonNull Application application) {
        super(application);

        googlePlacesRetrofitInstance = new GooglePlacesRetrofitInstance();
    }

    public Observable<LocationResultSet> getGooglePlacesData(String lat_long) {
        return  googlePlacesRetrofitInstance
                .getGooglePlacesData(lat_long, "1500", Constants.API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
