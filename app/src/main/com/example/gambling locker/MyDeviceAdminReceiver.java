package com.example.gamblingblocker;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.i("DeviceAdmin", "Admin enabled");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.i("DeviceAdmin", "Admin disabled");
    }
}
