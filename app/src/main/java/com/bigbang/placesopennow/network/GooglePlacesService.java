package com.bigbang.placesopennow.network;

import com.bigbang.placesopennow.model.LocationResultSet;
import com.bigbang.placesopennow.model.LocationResultSet;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.bigbang.placesopennow.util.Constants.URL_POSTFIX;
public interface GooglePlacesService {

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&opennow=true&key=

    @GET(URL_POSTFIX)
    Observable<LocationResultSet> getGooglePlacesData(@Query("location") String lat_long,
                                                      @Query("radius") String radius,
                                                      @Query("key") String api_key);
}
