package com.company.shidoris.butlertracker.activities;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.company.shidoris.butlertracker.R;
import com.company.shidoris.butlertracker.util.PermissionManager;
import com.company.shidoris.butlertracker.util.PermissionResult;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLocationService();
    }

    public void initLocationService(){

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("Loc: "+location.getLongitude());
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
        };

        PermissionManager.checkLocationPermission(this, locationManager, locationListener);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int grantResults[]){
        PermissionResult result = new PermissionResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PermissionManager.REQUEST_LOCATION_PERMISSION: {
                PermissionManager.locationResultProcess(result);
                return;
            }
        }
    }
}
