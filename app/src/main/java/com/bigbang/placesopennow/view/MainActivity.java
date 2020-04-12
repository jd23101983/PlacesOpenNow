package com.bigbang.placesopennow.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bigbang.placesopennow.R;
import com.bigbang.placesopennow.model.FireUser;
import com.bigbang.placesopennow.viewmodel.GooglePlacesViewModel;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private GooglePlacesViewModel googlePlacesViewModel;
    private LoginFragment loginFragment = new LoginFragment();
    private String username="JP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        googlePlacesViewModel = ViewModelProviders.of(this).get(GooglePlacesViewModel.class);

        if(googlePlacesViewModel.getUserLoggedIn()){
            // User Logged in
            setEmailAsUsername();
        } else {
            //User is not logged in - Display Login/Sign Up Fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.login_fragment_view, loginFragment)
                    .commit();
        }
    }


    private void setEmailAsUsername() {
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public void loginSuccess() {
        setEmailAsUsername();
        getSupportFragmentManager().beginTransaction()
                .remove(loginFragment)
                .commit();
    }

    @OnClick(R.id.view_map_button)
    public void viewMapClick(View view) {
        startActivity(new Intent(this, MapsFragment.class));
    }
}
