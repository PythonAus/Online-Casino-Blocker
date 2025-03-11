package com.example.gamblingblocker;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.util.Log;
import java.util.HashSet;
import java.util.Set;

public class ChromeMonitorService extends AccessibilityService {

    private static final String TAG = "ChromeMonitorService";
    private DevicePolicyManager dpm;
    private ComponentName deviceAdmin;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceAdmin = new ComponentName(this, MyDeviceAdminReceiver.class);
        Log.i(TAG, "Accessibility Service connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || event.getSource() == null) return;

        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null) return;

        CharSequence windowContent = traverseNodeForURL(nodeInfo);
        if (windowContent != null) {
            String contentString = windowContent.toString().toLowerCase();
            Set<String> blockedUrls = getBlockedUrls();
            for (String blockedUrl : blockedUrls) {
                if (contentString.contains(blockedUrl.toLowerCase())) {
                    Log.w(TAG, "Blocked URL detected: " + blockedUrl);
                    triggerDeviceWipe();
                    return;
                }
            }
        }
    }

    private CharSequence traverseNodeForURL(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo.getText() != null && nodeInfo.getText().length() > 0) {
            return nodeInfo.getText();
        }
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = nodeInfo.getChild(i);
            if (child == null) continue;

            CharSequence text = traverseNodeForURL(child);
            if (text != null && text.length() > 0) {
                return text;
            }
        }
        return null;
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "Accessibility Service interrupted");
    }

    private void triggerDeviceWipe() {
        if (dpm != null && dpm.isAdminActive(deviceAdmin)) {
            Log.w(TAG, "Initiating device wipe!");
            dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        } else {
            Log.e(TAG, "App is not device admin or DevicePolicyManager unavailable.");
        }
    }

    private Set<String> getBlockedUrls() {
        SharedPreferences prefs = getSharedPreferences("BlockedSites", MODE_PRIVATE);
        return prefs.getStringSet("blockedUrls", new HashSet<>());
    }
}
