package com.bigbang.placesopennow.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import com.bigbang.placesopennow.database.FavoritePlacesDAO;
import com.bigbang.placesopennow.database.FavoritePlacesDB;
import com.bigbang.placesopennow.database.FavoritePlacesEntity;
import com.bigbang.placesopennow.model.FireUser;
import com.bigbang.placesopennow.model.LocationResultSet;
import com.bigbang.placesopennow.network.GooglePlacesRetrofitInstance;
import com.bigbang.placesopennow.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GooglePlacesViewModel extends AndroidViewModel {

    private GooglePlacesRetrofitInstance googlePlacesRetrofitInstance;
    private FavoritePlacesDB favoritePlacesDB;
    private FavoritePlacesDAO favoritePlacesDAO;

    private MutableLiveData<Boolean> registrationMLD = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginMLD = new MutableLiveData<>();

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

    public void deleteFavoritePlace(FavoritePlacesEntity favoritePlacesEntity) {

        Log.d("TAG_XXX", "inside ViewModel::deleteFavoritePlace: " + favoritePlacesEntity.getPlaceName());

        favoritePlacesDB.getFavoritePlacesDAO().deleteFavoritePlace(favoritePlacesEntity);
    }

//    public List<FavoritePlacesEntity> getFavoritePlaces() {
//        return favoritePlacesDB.getFavoritePlacesDAO().selectAllFavoritePlaces();
//    }

    public Observable<List<FavoritePlacesEntity>> getFavoritePlaces() {
        return favoritePlacesDB
                .getFavoritePlacesDAO().selectAllFavoritePlaces()
                .observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io());
    }

    public Boolean getUserLoggedIn() {
        if (FirebaseAuth.getInstance()
                .getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
            return true;
        else
            return false;
    }

    public void loginUser(FireUser user) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(user.getUserName(), user.getUserPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.isSuccessful()) {

                            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                loginMLD.setValue(true);
                            } else
                                Toast.makeText(getApplication(), "Please verify email sent to " + user.getUserName(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(), "Login failed " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            loginMLD.setValue(false);
                        }
                    }
                });
    }

    public MutableLiveData<Boolean> getRegistrationStatus() {
        return registrationMLD;
    }

    public MutableLiveData<Boolean> getLoginStatus() {
        return loginMLD;
    }

    public void registerUser(FireUser user) {

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user.getUserName(), user.getUserPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.isSuccessful()) {
                            Toast.makeText(getApplication(), "User Creation Successful: Verification email sent.", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                            registrationMLD.setValue(true);
                        } else {

                            Toast.makeText(getApplication(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            registrationMLD.setValue(false);
                        }
                    }
                });
    }

}
