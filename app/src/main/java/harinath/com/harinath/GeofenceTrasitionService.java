package harinath.com.harinath;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import harinath.com.harinath.pojos.HistoryPojo;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.RequestBody;


public class GeofenceTrasitionService extends IntentService {

    private static final String TAG = GeofenceTrasitionService.class.getSimpleName();
    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    SessionManager mSessionManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng location_;
    private String transition_type = "";

    public GeofenceTrasitionService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve the Geofencing intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        mSessionManager = new SessionManager(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Handling errors
        if (geofencingEvent.hasError()) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMsg);
            return;
        }

        // Retrieve GeofenceTrasition
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            // Create a detail message with Geofences received
            String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences);
            // Send notification details as a String
            transition_type = geofenceTransitionDetails;
            sendNotification(geofenceTransitionDetails);
        }


        if (mSessionManager.getTYPE().equalsIgnoreCase("Child")) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    location_ = new LatLng(location.getLatitude(), location.getLongitude());

                    //if u comment below line, location wii be inserted for every 5-10 seconds
                    //   mFusedLocationClient.removeLocationUpdates(new LocationCallback());
                    registerReceiver(GeofenceTrasitionService.this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

                }
            });
        }


    }

    // Create a detail message with Geofences received
    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesList.add(geofence.getRequestId());
        }

        String status = null;
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            status = "Entering ";
        else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            status = "Exiting ";
        return status + TextUtils.join(", ", triggeringGeofencesList);
    }

    // Send a notification
    private void sendNotification(String msg) {
        Log.i(TAG, "sendNotification: " + msg);

        // Intent to start the main Activity
        Intent notificationIntent;
        if (mSessionManager.getTYPE().equalsIgnoreCase("Business unit")) {
            notificationIntent = new Intent(getApplicationContext(), OffersList.class);
        } else {
            notificationIntent = new Intent(getApplicationContext(), LocationHistory.class);

        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creating and sending Notification
        NotificationManager notificatioMng =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificatioMng.notify(
                GEOFENCE_NOTIFICATION_ID,
                createNotification(msg, notificationPendingIntent));
    }

    // Create a notification
    private Notification createNotification(String msg, PendingIntent notificationPendingIntent) {

        if (mSessionManager.getTYPE().equalsIgnoreCase("Child")) {
            sendFCMNotification(mSessionManager.getFB_ID(), msg);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_media_play_light)
                .setColor(Color.RED)
                .setContentTitle(msg)
                .setContentText("Geofence Notification!")
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder.build();

    }

    // Handle errors
    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }


    public void sendFCMNotification(String fcmkey, String type) {
        JSONObject jsonObjec = null;
        //  String bodydata = apiKey + "@#@" + sessionId + "@#@" + TOKEN;
        try {


            ArrayList<String> al = new ArrayList<>();
            al.add(fcmkey);
            jsonObjec = new JSONObject();
            JSONArray jsonArray = new JSONArray(al);
            jsonObjec.put("registration_ids", jsonArray);
            JSONObject jsonObjec2 = new JSONObject();
            jsonObjec2.put("message", "");
            jsonObjec2.put("purpose", "Geofence " + type);
            jsonObjec.put("data", jsonObjec2);

            jsonObjec.put("time_to_live", 172800);
            jsonObjec.put("priority", "HIGH");


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
        }


        Log.d("Shri", jsonObjec.toString());

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObjec.toString());
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=" + Constants.FCM_SERVER_KEY)
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

            }
        });
    }

    public void updateLocationInDB(HistoryPojo mHistoryPojo) {
        unregisterReceiver(GeofenceTrasitionService.this.mBatInfoReceiver);
        FirebaseDatabase.getInstance().getReference("LocationHistory").child(System.currentTimeMillis() + "").setValue(mHistoryPojo);
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            HistoryPojo mHistoryPojo = new HistoryPojo(mSessionManager.getFIRST_NAME(), System.currentTimeMillis() + "", transition_type,
                    mSessionManager.getPARENT_ID(), location_, level);
            updateLocationInDB(mHistoryPojo);
        }
    };
}