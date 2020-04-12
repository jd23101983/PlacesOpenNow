package com.bigbang.placesopennow.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigbang.placesopennow.R;
import com.bigbang.placesopennow.adapter.FavoritePlacesAdapter;
import com.bigbang.placesopennow.database.FavoritePlacesEntity;
import com.bigbang.placesopennow.util.DebugLogger;
import com.bigbang.placesopennow.viewmodel.GooglePlacesViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

public class FavoritePlacesFragment extends Fragment implements FavoritePlacesAdapter.FavoritePlaceInterface {

    private GooglePlacesViewModel googlePlacesViewModel;
    private FavoritePlacesAdapter favoritePlacesAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.favorite_places_recyclerview)
    RecyclerView favoritePlacesRecyclerView;

    public FavoritePlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_places, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        googlePlacesViewModel = ViewModelProviders.of(this).get(GooglePlacesViewModel.class);

/*
        compositeDisposable.add(googlePlacesViewModel.getFavoritePlaces().subscribe(newFavoritePlaceList -> {
            Log.d("TAG_X_Current", "New results in "+newFavoritePlaceList.size());
            displayFavoritePlaces(newFavoritePlaceList);
        }, throwable -> {
            DebugLogger.logError(throwable);
        }));
*/
        displayFavoritePlaces(googlePlacesViewModel.getFavoritePlaces());
    }

    public void displayFavoritePlaces (List<FavoritePlacesEntity> favoritePlacesEntityList) {
        favoritePlacesAdapter = new FavoritePlacesAdapter(favoritePlacesEntityList, this);
        favoritePlacesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoritePlacesRecyclerView.setAdapter(favoritePlacesAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void deleteFavoritePlace(FavoritePlacesEntity favoritePlacesEntity) {

        Log.d("TAG_XXX", "inside Fragment::deleteFavoritePlace: " + favoritePlacesEntity.getPlaceName());

        googlePlacesViewModel.deleteFavoritePlace(favoritePlacesEntity);
        displayFavoritePlaces(googlePlacesViewModel.getFavoritePlaces());
    }

    @OnClick(R.id.favorite_places_return_button)
    public void returnToMapFromFavorites(View view) {
        ((MapsFragment)getContext()).returnToMapFromFavorites();
    }
}
