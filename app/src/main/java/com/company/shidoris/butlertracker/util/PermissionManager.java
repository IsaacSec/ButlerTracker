package com.company.shidoris.butlertracker.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by isaac on 11/24/17.
 */

public class PermissionManager {

    public static final int REQUEST_LOCATION_PERMISSION = 1;

    public static void locationResultProcess(PermissionResult result){

        if (result.grantResults.length > 0 && result.grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Enable functionallity

        } else {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
        return;
    }

    public static void checkLocationPermission(Activity context, LocationManager manager, LocationListener listener){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)  != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    context,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    REQUEST_LOCATION_PERMISSION
            );

        } else {
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0,
                    listener
            );
        }
    }
}
