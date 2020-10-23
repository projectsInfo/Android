package com.karim.savelives;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mlastlocation;
    LocationRequest mLocationRequest;
    private static final String TAG = "Driver_Map_Activity";
    final int LOCATION_REQUEST_CODE = 1;
    private SupportMapFragment mapFragment;
    private String DonorUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ( SupportMapFragment ) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buildGoogleApiClient();

        getAssignDonor();
    }


    public void getAssignDonor() {
        String assignDonorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignDonorRef = FirebaseDatabase.getInstance().getReference().child("Users").child(assignDonorId);
        assignDonorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = ( Map<String, Object> ) dataSnapshot.getValue();
                    if (map.get("donorRequestId") != null) {
                        DonorUserId = map.get("donorRequestId").toString();
                        getAssignDonorPickupLocation();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getAssignDonorPickupLocation() {
        DatabaseReference assignDonorPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("DonorRequest").child(DonorUserId).child("l");
        assignDonorPickupLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Object> map = ( List<Object> ) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());

                    }

                    LatLng donorLatLng = new LatLng(locationLat, locationLng);


                    mMap.addMarker(new MarkerOptions().position(donorLatLng).title("Pickup Location"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }

        mMap.setMyLocationEnabled(true);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (getApplicationContext() != null) {
            if (location != null) {
                mlastlocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference refAvalibale = FirebaseDatabase.getInstance().getReference().child("DonorAvailble");
                DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference().child("DonorWorking");
                GeoFire geoFireAvalibale = new GeoFire(refAvalibale);
                GeoFire geoFireWorking = new GeoFire(refWorking);
                switch (DonorUserId) {
                    case "":
                        geoFireWorking.removeLocation(DonorUserId);
                        geoFireAvalibale.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                        break;
                    default:
                        geoFireAvalibale.removeLocation(DonorUserId);
                        geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));

                        break;
                }


                Log.i(TAG, "onLocationChanged55: " + location);

                Log.i(TAG, "onLocationChanged2: " + mlastlocation);
                Log.i(TAG, "onLocationChanged3: " + userId);

            }
        }


    }





    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        DatabaseReference refAvalibale = FirebaseDatabase.getInstance().getReference().child("DonorAvailble");
        DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference().child("DonorWorking");
        GeoFire geoFireAvalibale = new GeoFire(refAvalibale);
        GeoFire geoFireWorking = new GeoFire(refWorking);
        geoFireWorking.removeLocation(DonorUserId);
        geoFireAvalibale.removeLocation(DonorUserId);


    }

    protected void onDestroy(){
        super.onDestroy();
        DatabaseReference refAvalibale = FirebaseDatabase.getInstance().getReference().child("DonorAvailble");
        DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference().child("DonorWorking");
        GeoFire geoFireAvalibale = new GeoFire(refAvalibale);
        GeoFire geoFireWorking = new GeoFire(refWorking);
        geoFireWorking.removeLocation(DonorUserId);
        geoFireAvalibale.removeLocation(DonorUserId);
    }
}
