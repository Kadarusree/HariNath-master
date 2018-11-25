package harinath.com.harinath;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import harinath.com.harinath.pojos.Fencing;
import harinath.com.harinath.pojos.UnitLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final String TAG = "MapsActivity";
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        // Add a marker in Sydney and move the camera
      /*  LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/
        readFencings();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


    public void readFencings() {
        FirebaseDatabase mFirebaseDatabase
                = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mFirebaseDatabase.getReference("BusinessFencings");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot mnsp : dataSnapshot.getChildren()) {
                    Fencing mFencing = mnsp.getValue(Fencing.class);
                    drawGeofence(mFencing.getLocation(), mFencing.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Draw Geofence circle on GoogleMap

    private void drawGeofence(UnitLocation unitLocation, String name) {


        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(unitLocation.getLatitude(), unitLocation.getLongitude()))
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(200.0f);
       mMap.addCircle(circleOptions);

        MarkerOptions mMarker = new MarkerOptions();
        mMarker.position(new LatLng(unitLocation.getLatitude(), unitLocation.getLongitude()));
        mMarker.title(name);
        mMarker.icon(BitmapDescriptorFactory.defaultMarker());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new com.google.android.gms.maps.model.LatLng(unitLocation.getLatitude(), unitLocation.getLongitude()), 16));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
        mMap.addMarker(mMarker);
    }
}
