package harinath.com.harinath;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import harinath.com.harinath.pojos.Fencing;
import harinath.com.harinath.pojos.ShoolFencingPojo;

public class ChildActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "ParentDashboard";

    private static final long GEO_DURATION = 30 * 24 * 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private   float GEOFENCE_RADIUS = 200.0f; // in meters
    private final int GEOFENCE_REQ_CODE = 0;
    private PendingIntent geoFencePendingIntent;
    GoogleApiClient mGoogleApiClient;


    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Creating Google API Client");
        createGoogleApi();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mProgressDialog.dismiss();
        Toast.makeText(getApplicationContext(),"Sucess",Toast.LENGTH_SHORT).show();
        readFencings();
    }

    @Override

    public void onConnectionSuspended(int i) {
        mProgressDialog.dismiss();
        Toast.makeText(getApplicationContext(),"Failed, Login Again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mProgressDialog.dismiss();
        Toast.makeText(getApplicationContext(),"Failed, Login Again",Toast.LENGTH_SHORT).show();
    }


    public void readFencings() {
        mProgressDialog.setMessage("Searching for School Fence");
        mProgressDialog.show();
        FirebaseDatabase mFirebaseDatabase
                = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mFirebaseDatabase.getReference("SchoolFencing");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();
                if (dataSnapshot!=null) {
                    ShoolFencingPojo mFencing = dataSnapshot.getValue(ShoolFencingPojo.class);
                    GEOFENCE_RADIUS = mFencing.getRadius();
                    startGeofence(mFencing);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Start Geofence creation process
    private void startGeofence(ShoolFencingPojo fencing) {
        Log.i(TAG, "startGeofence()");
        Geofence geofence = createGeofence(new LatLng(fencing.getmLocation().getLatitude(), fencing.getmLocation().getLongitude()), GEOFENCE_RADIUS, fencing.getName());

        GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
        addGeofence(geofenceRequest);
    }

    private Geofence createGeofence(LatLng latLng, float radius, String name) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(name)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission()) {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    request,
                    createGeofencePendingIntent()
            );
        } else {
            askPermission();
        }
    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(ChildActivity.this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                ChildActivity.this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(ChildActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    int REQ_PERMISSION = 123;// Asks for permission

    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                ChildActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (mGoogleApiClient == null) {
            mProgressDialog.show();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }
}
