package com.bigbang.placesopennow.network;

import com.bigbang.placesopennow.model.LocationResultSet;
import com.bigbang.placesopennow.util.Constants;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GooglePlacesRetrofitInstance {

    private GooglePlacesService googlePlacesService;
    private OkHttpClient client;

    public GooglePlacesRetrofitInstance() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        googlePlacesService = createGooglePlacesService(getRetrofitInstance());
    }

    private Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private GooglePlacesService createGooglePlacesService(Retrofit retrofitInstance) {
        return retrofitInstance.create(GooglePlacesService.class);
    }

    public Observable<LocationResultSet> getGooglePlacesData(String lat_long, String radius, String api_key) {
        return googlePlacesService.getGooglePlacesData(lat_long, radius, api_key);
    }
}
