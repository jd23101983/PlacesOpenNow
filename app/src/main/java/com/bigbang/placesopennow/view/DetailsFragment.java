package com.bigbang.placesopennow.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigbang.placesopennow.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsFragment extends Fragment {

    private Marker marker;

    @BindView(R.id.details_textview)
    TextView fragmentTitle;

    @BindView(R.id.location_icon)
    ImageView locationIcon;

    @BindView(R.id.location_name)
    TextView locationName;

    public DetailsFragment(Marker marker) {
        // Required empty public constructor
        this.marker = marker;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Glide.with(this).load(marker.getSnippet()).into(locationIcon);
        locationName.setText(marker.getTitle());

        Log.d("TAG_XX", "DetailsFragment successfully created . . ." + marker.getTitle());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @OnClick(R.id.return_button)
    public void returnToMap(View view) {
        ((MapsFragment)getContext()).returnToMap();
    }
}
