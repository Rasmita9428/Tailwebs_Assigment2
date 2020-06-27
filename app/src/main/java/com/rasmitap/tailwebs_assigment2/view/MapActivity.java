package com.rasmitap.tailwebs_assigment2.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.rasmitap.tailwebs_assigment2.R;
import com.rasmitap.tailwebs_assigment2.utils.ConstantStore;
import com.rasmitap.tailwebs_assigment2.utils.GPSTracker;
import com.rasmitap.tailwebs_assigment2.utils.GlobalMethods;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;
    GPSTracker gpsTracker;
    LatLng current_latlng;
    Marker location_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map));
        gpsTracker = new GPSTracker(MapActivity.this);
        client = LocationServices.getFusedLocationProviderClient(this);
        checkPermission();

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
            if (GlobalMethods.isPermissionNotGranted(MapActivity.this, permissions)) {
                requestPermissions(permissions, ConstantStore.PERMISSION_CODE);
                return;
            } else {
                git remote add origin https://github.com/Rasmita9428/Tailwebs_Assigment2-Geolocation1.git              initMapConponet();
            }
        } else

            initMapConponet();
    }

    private void initMapConponet() {

        Log.e("test", "==>>initMapConponet() calll");

        Location nwLocation = gpsTracker
                .getLocation();

        if (nwLocation != null) {
            double latitude = nwLocation.getLatitude();
            double longitude = nwLocation.getLongitude();
            Toast.makeText(
                    getApplicationContext(),
                    "Mobile Location (NW): \nLatitude: " + latitude
                            + "\nLongitude: " + longitude,
                    Toast.LENGTH_LONG).show();

            final LatLng latLng = new LatLng(latitude, longitude);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    if (location != null) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                MarkerOptions marker = new MarkerOptions().position(latLng).title("I am Here");
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                googleMap.addMarker(marker);
                            }
                        });
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //  permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode) {
            case ConstantStore.PERMISSION_CODE:
                if (GlobalMethods.isPermissionNotGranted(MapActivity.this, permissions)) {
                    GlobalMethods.whichPermisionNotGranted(MapActivity.this, permissions, grantResults);
                } else {
                    //                    mapFragment.getMapAsync(ContactUsFragment.this);
                    Location nwLocation = gpsTracker
                            .getLocation();

                    if (nwLocation != null) {
                        double latitude = nwLocation.getLatitude();
                        double longitude = nwLocation.getLongitude();
                        Toast.makeText(
                                getApplicationContext(),
                                "Mobile Location (NW): \nLatitude: " + latitude
                                        + "\nLongitude: " + longitude,
                                Toast.LENGTH_LONG).show();
                        Log.e("latlong",latitude+"   "+ longitude);

                    }
                    initMapConponet();

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setCompassEnabled(false);

        Log.e("test", "==>>initMapConponet() calll");

        Location nwLocation = gpsTracker
                .getLocation();

        if (nwLocation != null) {
            double latitude = nwLocation.getLatitude();
            double longitude = nwLocation.getLongitude();
            Toast.makeText(
                    getApplicationContext(),
                    "Mobile Location (NW): \nLatitude: " + latitude
                            + "\nLongitude: " + longitude,
                    Toast.LENGTH_LONG).show();
 Log.e("latlong",latitude+"   "+ longitude);
            LatLng latLng = new LatLng(latitude, longitude);
                location_marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("I am Here").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                LatLngBounds.Builder bc = new LatLngBounds.Builder();
                bc.include(location_marker.getPosition());
                CameraPosition cameraPosition = CameraPosition.fromLatLngZoom(latLng, 10.0f);
                CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.animateCamera(cu);
                location_marker.showInfoWindow();


        }

    }
}