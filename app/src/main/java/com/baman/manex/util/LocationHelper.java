package com.baman.manex.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;


public class LocationHelper
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_CHECK_SETTINGS = 1001;

    private GoogleApiClient googleApiClient;

    private LocationCallback locationCallback;

    private LocationRequest mLocationRequest;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private long INTERVAL = (long) (1000 * 10);

    private long FASTEST_INTERVAL = (long) (1000 * 5);

    private Activity activity;

    LocCallBack locCallBack;

    public LocationHelper(Activity activity, LocCallBack locCallBack) {
        this.activity = activity;
        this.locCallBack = locCallBack;
        init();
    }

    public void onPause() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
        }
    }

    public void onResume() {
        if (googleApiClient != null) {
            googleApiClient.connect();
            createLocationRequest();
        }
    }

    public void onStart() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    public void init() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    public void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest
                .setInterval(INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        locationCallback = new LocationCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    return;
                }
                for (Location newLocation : locationResult.getLocations()) {
                    if (newLocation != null) {
                        locCallBack.updateUi(newLocation);
                    }
                }
            }
        };

        task.addOnFailureListener(activity, e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(activity,
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                }
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void setLocationProvider() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();
        setLocationProvider();
    }

    @Override
    public void onConnectionSuspended(int i) {
        locCallBack.updateUi(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        System.out.println(connectionResult.getErrorCode() + " " + connectionResult.getErrorMessage());

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(activity, 6);
            } catch (IntentSender.SendIntentException e) {
                locCallBack.updateUi(null);
            }
        } else {
            //Connection failure cannot be resolved
            Toast.makeText(activity,
                    "Unable to connect to Google Service, sorry >_<" + connectionResult.getErrorMessage(), Toast.LENGTH_LONG)
                    .show();
            Log.d("CONNECTION FAIL", connectionResult.getErrorCode() + " Unable to connect to Google Service, sorry >_<");
            locCallBack.updateUi(null);
        }

    }


}
