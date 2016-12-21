package com.assignment.wardrobe;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.assignment.wardrobe.utils.AlarmUtils;
import com.assignment.wardrobe.utils.AppConstants;
import com.assignment.wardrobe.utils.FileUtils;
import com.assignment.wardrobe.utils.NotificationHelper;

/**
 * Created by Rashmi on 20/12/16.
 */

public class WardrobeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtils.init(this, AppConstants.BASE_FOLDER_NAME);
        NotificationHelper.init(this);

        //set the alarm for daily notifications
        SharedPreferences  sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstAppOpen = sharedPrefs.getBoolean(AppConstants.SHARED_PREF_FIRST_APP_OPEN, true);
        if (firstAppOpen) {
            AlarmUtils.setAlarmForStyleCheck(this);
        }
        else {
            sharedPrefs.edit()
                    .putBoolean(AppConstants.SHARED_PREF_FIRST_APP_OPEN, false)
                    .apply();
        }
    }
}
