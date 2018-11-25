package harinath.com.harinath.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import harinath.com.harinath.ChildActivity;
import harinath.com.harinath.LocationHistory;
import harinath.com.harinath.LoginActivity;
import harinath.com.harinath.OffersList;
import harinath.com.harinath.R;

public class MessageingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() != null) {
            Map<String, String> data = remoteMessage.getData();
            if (data.get("purpose") != null) {
                String task = data.get("purpose");
                notifyTask(task, data);
            }
        }
    }

    private void notifyTask(String task, Map<String, String> data) {
        Intent intent = null;
        switch (task) {

            case "Enter":

                //new post
                intent = new Intent(getApplicationContext(), OffersList.class);
                showNotification(getApplicationContext(), data.get("type"), intent, data.get("username"));

                break;


            default:
                //not et decide
                intent = new Intent(getApplicationContext(), LocationHistory.class);
                showNotification(getApplicationContext(), "", intent, "");
                break;
        }
    }

    public void showNotification(Context context, String body, Intent intent, String username) {
        String channelId = "a3";
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_media_play_light)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentTitle("Notification from your child")
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "a3",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        try {
            notificationManager.notify(getNumber(), notificationBuilder.build());
        } catch (Exception e) {

        }
    }

    public int getNumber() {
        Random random = new Random();
        return random.nextInt(9999);
    }

    }
