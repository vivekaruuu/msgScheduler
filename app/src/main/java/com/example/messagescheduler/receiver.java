package com.example.messagescheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;

public class receiver extends BroadcastReceiver {
    private static final String TAG = "receiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        String number=intent.getStringExtra("number");
        String msg=intent.getStringExtra("msg");

        SmsManager smsManager = SmsManager.getDefault();
          smsManager.sendTextMessage(number, null, msg, null, null);
    }
}
