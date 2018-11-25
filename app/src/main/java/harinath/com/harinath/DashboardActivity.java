package harinath.com.harinath;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class DashboardActivity extends AppCompatActivity {
    int PLACE_PICKER_REQUEST = 1;
    LatLng location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }


    public void createFencing(View view) {
       /* */
        startActivity(new Intent(getApplicationContext(), CreateSchoolFencing.class));


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                location = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void launchUsers(View view) {
        startActivity(new Intent(getApplicationContext(), UsersListActivity.class));
    }

    public void addOffer(View view) {
        startActivity(new Intent(getApplicationContext(), AddOffer.class));

    }

    public void viewOffers(View view) {
        startActivity(new Intent(getApplicationContext(), OffersList.class));

    }

    public void add_child(View view) {
        startActivity(new Intent(getApplicationContext(), AddChlid.class));
    }

    public void viewFencing(View view) {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }
}
