package com.bigbang.placesopennow.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import android.util.Log;
import com.bigbang.placesopennow.R;
import com.bigbang.placesopennow.model.LocationResultSet;
import com.bigbang.placesopennow.model.Result;
import com.bigbang.placesopennow.util.DebugLogger;
import com.bigbang.placesopennow.viewmodel.GooglePlacesViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import static com.bigbang.placesopennow.util.Constants.REQUEST_CODE;
import static com.bigbang.placesopennow.util.DebugLogger.logDebug;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GooglePlacesViewModel googlePlacesViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location currentLocation;

    private DetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        googlePlacesViewModel = ViewModelProviders.of(this).get(GooglePlacesViewModel.class);
        setUpLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //check if permission was granted
        //Step 1
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG_X", "PERMISSION GRANTED");
            setUpLocation();
            //setVisibilityGone();
        } else {
            Log.d("TAG_X", "PERMISSION NOT GRANTED");
            //Step 2 if is not granted
            requestPermissions();
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    setUpLocation();
                else { //Permission was denied
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                        requestPermissions();
                    else
                        Log.d("TAX_X", "show requirements needed . . . "); //showRequirements();
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setPoiClick(mMap);
    }

    @SuppressLint("MissingPermission")
    private void setUpLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                10,
                this);
    }

    public void setupCurrentMap(LatLng currentLatLng) {
        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Marker at Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
    }

    @SuppressLint("MissingPermission")
    public void getCurrentOpenLocations() {

        String coordinates;

        if (currentLocation != null)
            coordinates = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        else {
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            coordinates = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        }

        compositeDisposable.add(googlePlacesViewModel.getGooglePlacesData(coordinates).subscribe(googlePlacesResults -> {
            displayInformationRx(googlePlacesResults);
        }, throwable -> {
            DebugLogger.logError(throwable);
        }));
    }

    private void displayInformationRx(LocationResultSet googlePlacesResults) {

        List<Result> results = googlePlacesResults.getResults();

        for (int i = 0; i < results.size(); i++) {

            if (results.get(i) != null) {
                logDebug("RxJava : " + results.get(i).getName());
                logDebug("RxJava : " + results.get(i).getGeometry().getLocation().getLat());
                logDebug("RxJava : " + results.get(i).getGeometry().getLocation().getLng());
                logDebug("RxJava : " + results.get(i).getIcon());
                logDebug("RxJava : " + results.get(i).getOpeningHours());


                addLocationMarker(results.get(i).getName(), results.get(i).getGeometry().getLocation().getLat(),
                        results.get(i).getGeometry().getLocation().getLng(), results.get(i).getIcon());
            }
        }
    }

    public void addLocationMarker(String name, Double latitude, Double longitude, String icon) {

        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(name).snippet(icon));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Log.d("TAG_XX", "Marker Clicked: " + marker.getTitle() + " " + marker.getId() + " " + marker.getPosition());
                detailsFragment = new DetailsFragment(marker);
                loadDetailsFragment();
                return true;
            }
        });
    }

    private void setPoiClick(final GoogleMap map) {

        Log.d("TAG_XX", "inside POI Click A");

        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions()
                        .position(poi.latLng)
                        .title(poi.name));
                poiMarker.showInfoWindow();

                poiMarker.setTag("poi");

                Log.d("TAG_XX", "inside POI Click B");
            }
        });
    }

    public void loadDetailsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.multi_fragment_frame, detailsFragment)
                .commit();
    }

    public void returnToMap() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(detailsFragment)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationManager != null)
            locationManager.removeUpdates(this); //This will also stop memory leaks....
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("TAG_X", "LOCATION : " + location.getLatitude() + "," + location.getLongitude());
        currentLocation = location;
        setupCurrentMap(new LatLng(location.getLatitude(), location.getLongitude()));
        getCurrentOpenLocations();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
