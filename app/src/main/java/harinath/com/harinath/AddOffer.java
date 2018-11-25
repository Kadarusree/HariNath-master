package harinath.com.harinath;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//A commnet for Pull request
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import harinath.com.harinath.pojos.OfferPojo;
import harinath.com.harinath.pojos.UserRegPojo;

public class AddOffer extends AppCompatActivity {


    TextView mBigText, mSmallText;
    EditText offerTitle, Description;


    Spinner businessunits;

    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mDatabaseReference;

    private ProgressDialog mProgressDialog;
    ArrayList<String> mBusinessunits;

    String unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        mBigText = (TextView) findViewById(R.id.big_text);
        mSmallText = (TextView) findViewById(R.id.small_text);
        Typeface tf = Typeface.createFromAsset
                (getAssets(), "BigTetx.ttf");
        Typeface tf2 = Typeface.createFromAsset
                (getAssets(), "SmallText.ttf");
        mBigText.setTypeface(tf);
        mSmallText.setTypeface(tf2);
        mBusinessunits = new ArrayList<>();
        offerTitle = findViewById(R.id.edt_offer_title);
        Description = findViewById(R.id.edt_offer_description);
        businessunits = findViewById(R.id.spn_businessUnits);

        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabse.getReference();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Authenticating...");
        mProgressDialog.setCancelable(false);
        loadBusinessUnits();


    }

    public void saveOffer(View view) {
        mProgressDialog.setMessage("Creating offer");
        mProgressDialog.show();
        OfferPojo mOffer = new OfferPojo(offerTitle.getText().toString(),
                Description.getText().toString(),
                unit);
        mDatabaseReference.child("Offers").child(mDatabaseReference.push().getKey()).setValue(mOffer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Added Sucessfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void loadBusinessUnits() {
        mProgressDialog.setMessage("Loading Business Units");
        mProgressDialog.show();
        mDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBusinessunits.clear();
                mProgressDialog.dismiss();
                if (dataSnapshot != null) {
                    for (DataSnapshot dp : dataSnapshot.getChildren()) {
                        UserRegPojo mUserRegPojo = dp.getValue(UserRegPojo.class);

                        if (mUserRegPojo.getType().equalsIgnoreCase("Business unit")) {
                            mBusinessunits.add(mUserRegPojo.getBusinessName());
                        }
                    }

                    ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spin_item, mBusinessunits);
                    businessunits.setAdapter(adp);

                    if (mBusinessunits.size() > 0) {
                        businessunits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                unit = mBusinessunits.get(i);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
