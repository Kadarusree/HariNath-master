package harinath.com.harinath;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import harinath.com.harinath.pojos.UnitLocation;
import harinath.com.harinath.pojos.UserRegPojo;


public class Signup extends AppCompatActivity {

    TextView mBigText, mSmallText, btnSignup;


    ImageView pickLocation;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog mProgressDialog;

    int PLACE_PICKER_REQUEST = 1;
    UnitLocation location = null;

    EditText firstName, lastName, email, mobile_number, password, confirmPassword, businsessName, businessUnitLocationName;

    Spinner reg_type;
    String type = "";

    LinearLayout mBusinesslayout, mBusinessLocationLayout;

    SharedPreferences mSharedPreferences;
    String fb_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        mBigText = (TextView) findViewById(R.id.big_text);
        mSmallText = (TextView) findViewById(R.id.small_text);
        pickLocation = (ImageView) findViewById(R.id.pick_location);
        Typeface tf = Typeface.createFromAsset
                (getAssets(), "BigTetx.ttf");
        Typeface tf2 = Typeface.createFromAsset
                (getAssets(), "SmallText.ttf");
        mBigText.setTypeface(tf);
        mSmallText.setTypeface(tf2);

        mSharedPreferences = getSharedPreferences("FB_KEY", CONTEXT_IGNORE_SECURITY);
        fb_key = mSharedPreferences.getString("key", "");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabse.getReference("Users");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Authenticating...");
        mProgressDialog.setCancelable(false);


        firstName = findViewById(R.id.edt_first_name);
        lastName = findViewById(R.id.edt_last_name);
        mobile_number = findViewById(R.id.edt_mobile_number);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        confirmPassword = findViewById(R.id.edt_password);
        reg_type = findViewById(R.id.reg_type);
        mBusinesslayout = findViewById(R.id.layout_business_unit);
        mBusinessLocationLayout = findViewById(R.id.layout_business_unit_location);
        businsessName = findViewById(R.id.edt_business_unit_name);
        businessUnitLocationName = findViewById(R.id.edt_business_unit_location);
        reg_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        type = "";
                        mBusinesslayout.setVisibility(View.INVISIBLE);
                        mBusinessLocationLayout.setVisibility(View.INVISIBLE);
                        location = null;
                        break;
                    case 1:
                        type = "Business unit";
                        mBusinesslayout.setVisibility(View.VISIBLE);
                        mBusinessLocationLayout.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        type = "Parent";
                        location = null;
                        mBusinesslayout.setVisibility(View.INVISIBLE);
                        mBusinessLocationLayout.setVisibility(View.INVISIBLE);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnSignup = findViewById(R.id.tv_signup);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validadtions()) {
                    signUp(email.getText().toString(), password.getText().toString());

                }
            }
        });

        pickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(Signup.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void signUp(final String email, final String password) {
        mProgressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    mProgressDialog.setMessage("Creating Account");
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserRegPojo mUser = new UserRegPojo(firstName.getText().toString().trim(),
                            lastName.getText().toString().trim(),
                            password, mobile_number.getText().toString().trim(),
                            email, type, businsessName.getText().toString(), location, fb_key, user.getUid());
                    mProgressDialog.show();
                    mDatabaseReference.child(user.getUid()).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mProgressDialog.dismiss();
                            if (task.isSuccessful()) {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Signup.this);
                                mBuilder.setMessage("Account Created Sucessfully");
                                mBuilder.setCancelable(false);
                                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });
                                mBuilder.show();
                            }
                        }
                    });
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(Signup.this, "Someone is already using the mail",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validadtions() {
        boolean isValid = true;
        if (!isValidEmail(email.getText().toString().trim())) {
            email.setError("Invalid Email");
            isValid = false;
        }
        if (firstName.getText().toString().length() < 1) {
            firstName.setError("First Name must be minimum 1 character");
            isValid = false;
        }
        if (lastName.getText().toString().length() < 3) {
            lastName.setError("Last Name must be minimum 3 characters");
            isValid = false;
        }
        if (mobile_number.getText().toString().length() < 10) {
            mobile_number.setError("Enter a valid mobile number");
            isValid = false;
        }
        if (password.getText().toString().length() < 6) {
            password.setError("Password must be minimum 6 characters");
            isValid = false;
        }
        if (type.equalsIgnoreCase("")) {
            isValid = false;
            Toast.makeText(getApplicationContext(), "Select Type", Toast.LENGTH_LONG).show();
        }
        if (confirmPassword.equals(password.getText().toString())) {
            confirmPassword.setError("Passwords Doesnt Match");
            isValid = false;
        }
        if (type.equalsIgnoreCase("Business unit") && businsessName.getText().toString().length() < 3) {
            businsessName.setError("Enter your business unit name");
            isValid = false;
        }

        return isValid;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                location = new UnitLocation(place.getLatLng().latitude, place.getLatLng().longitude);
                businessUnitLocationName.setText(String.format("%s", place.getName()));
               /* String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();*/
            }
        }
    }
}
