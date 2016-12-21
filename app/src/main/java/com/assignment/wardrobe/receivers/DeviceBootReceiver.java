package com.assignment.wardrobe.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.assignment.wardrobe.utils.AlarmUtils;

public class DeviceBootReceiver extends BroadcastReceiver {
    public DeviceBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmUtils.setAlarmForStyleCheck(context);
    }
}
