package com.example.carparts;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Geolocation extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mUiSettings = mMap.getUiSettings();
        // Add a marker in Sydney and move the camera
        LatLng carparts = new LatLng(51.7767068, 19.488359);
        mMap.addMarker(new MarkerOptions().position(carparts).title("Carparts shop"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(carparts));

        LatLng intercars = new LatLng(51.8054863, 19.4195499);
        mMap.addMarker(new MarkerOptions().position(intercars).title("InterCars").snippet("intercars.pl"));

        LatLng iparts = new LatLng(51.7843386,19.4572096);
        mMap.addMarker(new MarkerOptions().position(iparts).title("IParts").snippet("iparts.pl"));

        LatLng ucando = new LatLng(51.796535, 19.3851062);
        mMap.addMarker(new MarkerOptions().position(ucando).title("Ucando").snippet("ucando.pl"));

        LatLng autocraft24 = new LatLng(51.7842983, 19.4511713);
        mMap.addMarker(new MarkerOptions().position(autocraft24).title("Autocraft24").snippet("autocraft24.pl"));


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
             @Override
             public void onInfoWindowClick(Marker marker) {
                 if(marker != null && marker.getTitle().equals("InterCars")){
                     goToUrl ( "https://intercars.pl/");
                 }
                 if(marker != null && marker.getTitle().equals("IParts")){
                     goToUrl ( "https://www.iparts.pl/");
                 }
                 if(marker != null && marker.getTitle().equals("Ucando")){
                     goToUrl ( "https://www.ucando.pl/");
                 }
                 if(marker != null && marker.getTitle().equals("Autocraft24")){
                     goToUrl ( "https://autocraft24.pl/");
                 }
             }
         });

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMapToolbarEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
    }
    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}
