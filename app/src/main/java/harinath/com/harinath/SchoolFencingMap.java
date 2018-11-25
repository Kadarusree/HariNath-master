package harinath.com.harinath;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import harinath.com.harinath.pojos.Fencing;
import harinath.com.harinath.pojos.ShoolFencingPojo;
import harinath.com.harinath.pojos.UnitLocation;

public class SchoolFencingMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_fencing_map);
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

      readFencings();
    }


    public void readFencings() {
        FirebaseDatabase mFirebaseDatabase
                = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mFirebaseDatabase.getReference("SchoolFencing");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    ShoolFencingPojo mFencing = dataSnapshot.getValue(ShoolFencingPojo.class);
                    drawGeofence(mFencing.getmLocation(), mFencing.getName(),mFencing.getRadius() );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Draw Geofence circle on GoogleMap

    private void drawGeofence(UnitLocation unitLocation, String name, float radius) {


        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(unitLocation.getLatitude(), unitLocation.getLongitude()))
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(radius);
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
