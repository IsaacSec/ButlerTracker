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
import com.company.shidoris.butlertracker.model.Request;
import com.company.shidoris.butlertracker.model.RequestsData;
import com.company.shidoris.butlertracker.util.PermissionManager;
import com.company.shidoris.butlertracker.util.PermissionResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    private DatabaseReference database;

    @BindView(R.id.available_requests) LinearLayout availableRequests;
    @BindView(R.id.accepted_requests)  LinearLayout acceptedRequests;

    private ArrayList<Request> acceptedRequestList;

    private RequestsData databaseRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initLocationService();

        availableRequests.addView(createAvailableRequestView("Hola"));

        acceptedRequestList = new ArrayList<>();
        initDatabase();
    }

    public void initLocationService(){

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String lat = ""+location.getLatitude();
                String lon = ""+location.getLongitude();
                System.out.println("Loc: "+lat+" "+lon);
                
                Map<String, Request> map = databaseRequests.getRequestsdata();

                for (Map.Entry<String, Request> entry : map.entrySet()){
                    Request req = entry.getValue();

                    if ( req.getStatus().equals("Accepted")){
                        database.child("requestsdata").child(req.getId()).child("deliverLat").setValue(lat);
                        database.child("requestsdata").child(req.getId()).child("deliverLon").setValue(lon);
                    }
                }
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

        ValueEventListener reqListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Refreshing");

                RequestsData data = dataSnapshot.getValue(RequestsData.class);
                databaseRequests = data;
                ArrayList<String> avaRequests = new ArrayList<>();
                ArrayList<String> accRequests = new ArrayList<>();
                Map<String, Request> map = data.getRequestsdata();

                for (Map.Entry<String, Request> entry : map.entrySet()){
                    Request req = entry.getValue();
                    if ( req.getStatus().equals("Deliver") ){
                        avaRequests.add(req.getId());
                    }
                    if ( req.getStatus().equals("Accepted")){
                        accRequests.add(req.getId());
                    }
                }

                refreshAvailableRequests(avaRequests);
                refreshAcceptedRequests(accRequests);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database.addValueEventListener(reqListener);
    }

    public void refreshAvailableRequests(ArrayList<String> reqIds){
        availableRequests.removeAllViews();

        for (String id: reqIds){
            availableRequests.addView(createAvailableRequestView(id));
        }
    }

    public void refreshAcceptedRequests(ArrayList<String> reqIds){
        acceptedRequests.removeAllViews();

        for (String id: reqIds) {
            acceptedRequests.addView(createAcceptedRequestView(id));
        }
    }

    public View createAvailableRequestView(final String title){
        View view = getLayoutInflater().inflate(R.layout.request_view, null);
        TextView tv = view.findViewById(R.id.request_title);
        tv.setText(title);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child("requestsdata").child(title).child("status").setValue("Accepted");
            }
        });

        return view;
    }

    public View createAcceptedRequestView(final String title){
        View view = getLayoutInflater().inflate(R.layout.request_view, null);
        TextView tv = view.findViewById(R.id.request_title);
        tv.setText(title);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child("requestsdata").child(title).child("status").setValue("Deliver");
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
