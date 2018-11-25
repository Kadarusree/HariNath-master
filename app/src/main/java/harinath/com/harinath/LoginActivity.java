package harinath.com.harinath;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import harinath.com.harinath.pojos.UserRegPojo;


public class LoginActivity extends AppCompatActivity {

    TextView mBigText, mSmallText;
    EditText username, password;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mDatabaseReference;

    private ProgressDialog mProgressDialog;

    private  SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mBigText = (TextView) findViewById(R.id.big_text);
        mSmallText = (TextView) findViewById(R.id.small_text);
        Typeface tf = Typeface.createFromAsset
                (getAssets(), "BigTetx.ttf");
        Typeface tf2 = Typeface.createFromAsset
                (getAssets(), "SmallText.ttf");
        mBigText.setTypeface(tf);
        mSmallText.setTypeface(tf2);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabse.getReference("Users");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Authenticating...");
        mProgressDialog.setCancelable(false);
        mSessionManager = new SessionManager(this);
    }

    public void login(View view) {


        if (validations())
        {
            mProgressDialog.show();
            mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressDialog.dismiss();
                            mProgressDialog.setMessage("Signing In");
                            if (task.isSuccessful()) {
                                mProgressDialog.show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                mDatabaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        mProgressDialog.dismiss();
                                        if (dataSnapshot != null) {
                                            UserRegPojo mUser = dataSnapshot.getValue(UserRegPojo.class);
                                            Toast.makeText(getApplicationContext(), "Welcome " + mUser.getFirstname() + "", Toast.LENGTH_LONG).show();

                                            Constants.username = mUser.getFirstname() + "/" + mUser.getLastname();
                                            Constants.currentUser = mUser;
                                            mSessionManager.createSession(mUser);

                                            if (mUser.getType().equalsIgnoreCase("Parent")) {
                                                startActivity(new Intent(getApplicationContext(), ParentDashboard.class));
                                            }
                                            else if(mUser.getType().equalsIgnoreCase("Child")) {
                                                startActivity(new Intent(getApplicationContext(), ChildActivity.class));
                                            }
                                                else
                                             {
                                                startActivity(new Intent(getApplicationContext(), BusinessDashboard.class));
                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        mProgressDialog.dismiss();
                                    }
                                });
                                // updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                //   Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }

                            // ...
                        }
                    });
        }


    }

    public void signup(View view) {
        startActivity(new Intent(getApplicationContext(), Signup.class));

    }


    public void AdminLogin(View view) {
        if (username.getText().toString().equalsIgnoreCase("admin")&&password.getText().toString().trim().equalsIgnoreCase("1234")){
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        }
        else {
            Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checklocation();
    }

    public void checklocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
            dialog.setMessage("Please Enable Location");
            dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {


                }
            });
            dialog.show();
        }
    }

    public void forgotPassword(View view) {
        mProgressDialog.show();
        FirebaseAuth.getInstance().sendPasswordResetEmail(username.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Email Sent to reset password", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Email is not registered", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public boolean validations() {
        boolean valid = true;
        if (username.getText().toString().trim().equalsIgnoreCase("")) {
            valid = false;
            username.setError("Enter Registered Email");
        }
        if (password.getText().toString().trim().equalsIgnoreCase("")) {
            valid = false;
            password.setError("Enter Password");

        }

        return valid;

    }

    public void save(View view) {
    }
}
