package co.crazytech.gcmtest;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by eric on 2/27/2015.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIF_ID = 1;
    private NotificationManager notifMan;
    private NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("co.crazytech.gcmtest.GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if(!extras.isEmpty()){
            String appName = getApplicationContext().getString(R.string.app_name);
            if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
                sendNotification(appName,"Send error "+extras.toString());
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
                sendNotification(appName,"Deleted messages on server: "+extras.toString());
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
//                for (int i = 0; i < 5; i++) {
//                    Log.i(MainActivity.LOG_TAG, "Working... " + (i+1)+"/5 @ "+ SystemClock.elapsedRealtime());
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                Log.i(MainActivity.LOG_TAG,"Completed work @ "+SystemClock.elapsedRealtime());
                sendNotification(extras.getString("title"),extras.getString("message"));
                Log.i(MainActivity.LOG_TAG,"Received: "+extras.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String title, String msg){
        notifMan = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this,0,new Intent(this,MainActivity.class),0);
        NotificationCompat.Builder currBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setContentTitle(title)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
            .setContentText(msg);

        currBuilder.setContentIntent(contentIntent);
        notifMan.notify(NOTIF_ID, currBuilder.build());
    }
}
