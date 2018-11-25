package harinath.com.harinath;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;

import harinath.com.harinath.pojos.UserRegPojo;

public class AddChlid extends AppCompatActivity {


    EditText firstName, lastName, mobile_number, email, password;
    Spinner parent;

    FirebaseDatabase mFirebaseDatabse;
    DatabaseReference mDatabaseReference;

    ArrayList<UserRegPojo> mUsers;
    ArrayList<String> mNames;

    ArrayAdapter<String> adapter;

    private ProgressDialog mProgressDialog;
    SharedPreferences mSharedPreferences;
    String fb_key, parent_name, parentID;
    FirebaseAuth mAuth;

    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chlid);

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


        parent = findViewById(R.id.spn_parentNames);



        mDatabaseReference = mFirebaseDatabse.getReference("Users");
        mUsers = new ArrayList<>();
        mNames = new ArrayList<>();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.clear();
                mNames.clear();
                mNames.add("Select");
                for (DataSnapshot dpst : dataSnapshot.getChildren()) {


                    if (dpst.getValue(UserRegPojo.class).getType().equalsIgnoreCase("parent"))
                    {
                        mUsers.add(dpst.getValue(UserRegPojo.class));
                        mNames.add(dpst.getValue(UserRegPojo.class).getFirstname());
                    }


                }

                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spin_item, mNames);
                parent.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                parent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        pos = i;
                        if (i>0){
                            mobile_number.setText(mUsers.get(i-1).getMobileNumber());
                            parent_name = mNames.get(i);
                            fb_key = mUsers.get(i-1).getFb_key();
                            parentID = mUsers.get(i-1).getmyKey();
                        }
                        else {
                            mobile_number.setText("");
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        if (pos==0){
Toast.makeText(getApplicationContext(),"Select Parent",Toast.LENGTH_SHORT).show();
        }


        return isValid;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void save(View view) {
        if (validadtions()){
            mProgressDialog.show();
            mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressDialog.dismiss();
                    if (task.isSuccessful()) {
                        mProgressDialog.setMessage("Creating Account");
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserRegPojo mUser = new UserRegPojo(firstName.getText().toString().trim(),
                                lastName.getText().toString().trim(),
                                password.getText().toString().trim(), mobile_number.getText().toString().trim(),
                                email.getText().toString().trim(), "child", parentID, null, fb_key, user.getUid());
                        mProgressDialog.show();
                        mDatabaseReference.child(user.getUid()).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mProgressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddChlid.this);
                                    mBuilder.setMessage("Child Added Sucessfully");
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
                        Toast.makeText(AddChlid.this, "Someone is already using the mail",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
