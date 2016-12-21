package com.assignment.wardrobe.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.assignment.wardrobe.R;
import com.assignment.wardrobe.view.activities.HomeActivity_;

/**
 * Created by Rashmi on 21/12/16.
 */

public class NotificationHelper {

    private Context context;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;

    private static NotificationHelper mNotificationHelper;

    private NotificationHelper(Context context) {
        this.context = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationBuilder = new NotificationCompat.Builder(context);
        mNotificationBuilder.setSmallIcon(R.drawable.ic_notification);
        mNotificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS
                | Notification.DEFAULT_SOUND
                | Notification.FLAG_AUTO_CANCEL);

        Intent resultIntent = new Intent(context, HomeActivity_.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity_.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationBuilder.setContentIntent(resultPendingIntent);
    }

    public static void init(Context context) {
        mNotificationHelper = new NotificationHelper(context);
    }

    public static NotificationHelper getInstance() {
        if (mNotificationHelper == null) {
            throw new RuntimeException("NotificationHelper not initialized. Call init() first");
        }
        return mNotificationHelper;
    }

    public void displaySimpleNotification(int notificationId, String title, String body) {
        mNotificationBuilder.setContentTitle(title);
        mNotificationBuilder.setContentText(body);
        mNotificationManager.notify(notificationId, mNotificationBuilder.build());
    }

    public void clearAllNotifications() {
        mNotificationManager.cancelAll();
    }
}
