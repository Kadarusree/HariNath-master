package harinath.com.harinath.firebase;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by srikanthk on 6/29/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String TAG = "MyFirebaseInstanceIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);


        SharedPreferences mSharedPreferences = getSharedPreferences("FB_KEY",CONTEXT_IGNORE_SECURITY);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("key", refreshedToken);
        mEditor.commit();
    }
}