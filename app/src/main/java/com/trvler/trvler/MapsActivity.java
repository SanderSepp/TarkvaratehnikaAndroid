package com.trvler.trvler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;


import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        LatLng coordinates = new LatLng(59.3971713, 24.66111509999996);
        createMarkers(mMap);
        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(coordinates)
            .zoom(15.0f)
            .tilt(30)
            .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("markers.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public void createMarkers(GoogleMap mMap) {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray markersArray = obj.getJSONArray("markers");

            for (int i = 0; i < markersArray.length(); i++) {
                JSONObject jo_inside = markersArray.getJSONObject(i);
                //Log.d("Latitude: -->", jo_inside.getString("lat"));
                Double lat = jo_inside.getDouble("lat");
                Double lng = jo_inside.getDouble("lng");
                String title = jo_inside.getString("title");
                String description = jo_inside.getString("description");
                mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(title)
                    .snippet(description));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}