package sa.upscale.coworking.Notifications;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.R;

import static android.text.TextUtils.isEmpty;

/**
 * Created by bhatt on 1/12/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;
    String str_title, message, blog_id = "", blog_title = "", blog_image = "", blog_description = "";
    String channelId = "channel-01";
    String channelName = "Channel Name";
    int importance = NotificationManager.IMPORTANCE_HIGH;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo1);
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            message = remoteMessage.getNotification().getBody();
        } else {
            str_title = remoteMessage.getData().get("subject");
            message = remoteMessage.getData().get("description");
            blog_id = remoteMessage.getData().get("blog_id");
            blog_title = remoteMessage.getData().get("blog_title");
            blog_image = remoteMessage.getData().get("blog_image");
            blog_description = remoteMessage.getData().get("blog_description");
        }

        Notification(message);
    }

    private void Notification(String messageBody) {
        if (isEmpty(blog_id)) {
            launchActivity(NavigationActivity.class, messageBody, "1");
        } else {
            launchActivity(NavigationActivity.class, messageBody, "2");
        }
    }

    public void launchActivity(Class<?> intentClass, String messageBody, String flag) {
        Intent intent = new Intent(this, intentClass);
        if (flag.equals("2")) {
            intent.putExtra("id", blog_id);
            intent.putExtra("name", "");
            intent.putExtra("title", blog_title);
            intent.putExtra("desc", blog_description);
            intent.putExtra("image", blog_image);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(bitmap)/*Notification icon image*/
                .setContentTitle(str_title)
                .setChannelId(channelId)
                .setSmallIcon(R.drawable.logo1)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        notificationManager.notify(m, notificationBuilder.build());
    }

}
