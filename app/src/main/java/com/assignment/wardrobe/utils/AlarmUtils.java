package com.assignment.wardrobe.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.assignment.wardrobe.receivers.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by Rashmi on 06/06/16.
 */
public final class AlarmUtils {

    private static final String TAG = AlarmUtils.class.getSimpleName();

    /**
     * Helper method to set an alarm for the same time everyday
     *
     * @param context
     */
    public static final void setAlarmForStyleCheck(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        Calendar alarmCalendar = Calendar.getInstance();
        //if today's alarm time has passed already then set alarm for tomorrow
        if (hours >= AppConstants.ALARM_HOURS && minutes >= AppConstants.ALARM_MINUTES) {
            alarmCalendar.set(Calendar.HOUR_OF_DAY, AppConstants.ALARM_HOURS);
            alarmCalendar.set(Calendar.MINUTE, AppConstants.ALARM_MINUTES);
            alarmCalendar.add(Calendar.HOUR_OF_DAY, 24);
        }
        //if today's time has not passed already then set alarm
        else {
            alarmCalendar.set(alarmCalendar.HOUR_OF_DAY, AppConstants.ALARM_HOURS);
            alarmCalendar.set(Calendar.MINUTE, AppConstants.ALARM_MINUTES);
        }
        Log.e(TAG, "Alarm set for: " + alarmCalendar.toString() + "--" + alarmCalendar.getTimeInMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
        }
        else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
        }
    }
}
