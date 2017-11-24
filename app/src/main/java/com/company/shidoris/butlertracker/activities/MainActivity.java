package com.company.shidoris.butlertracker.activities;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.shidoris.butlertracker.R;
import com.company.shidoris.butlertracker.util.PermissionManager;
import com.company.shidoris.butlertracker.util.PermissionResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    private DatabaseReference database;

    @BindView(R.id.available_requests) LinearLayout availableRequests;
    @BindView(R.id.accepted_requests)  LinearLayout acceptedRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initLocationService();

        availableRequests.addView(createRequestView("Hola"));
        availableRequests.addView(createRequestView("Hola1"));
        availableRequests.addView(createRequestView("Hola2"));
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

    public void initDatabase(){
        database = FirebaseDatabase.getInstance().getReference();
    }

    public View createRequestView(final String title){
        View view = getLayoutInflater().inflate(R.layout.request_view, null);
        TextView tv = view.findViewById(R.id.request_title);
        tv.setText(title);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Accept request
            }
        });

        return view;
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
