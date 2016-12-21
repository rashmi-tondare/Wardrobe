package com.assignment.wardrobe.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.assignment.wardrobe.utils.AppConstants;
import com.assignment.wardrobe.utils.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Log.e(TAG, "alarm received");

            NotificationHelper.getInstance().displaySimpleNotification(AppConstants.NEW_STYLE_NOTIFICATION_ID,
                    AppConstants.NEW_STYLE_NOTIFICATION_TITLE,
                    AppConstants.NEW_STYLE_NOTIFICATION_CONTENT);
        }
    }
}
