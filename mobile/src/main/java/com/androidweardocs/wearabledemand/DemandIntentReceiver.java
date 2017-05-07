package com.androidweardocs.wearabledemand;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by mike on 4/1/17.
 */

public class DemandIntentReceiver extends BroadcastReceiver{

    public static final String ACTION_DEMAND = "com.androidweardocs.ACTION_DEMAND";
    public static final String EXTRA_MESSAGE = "com.androidweardocs.EXTRA_MESSAGE";
    public static final String EXTRA_VOICE_REPLY = "com.androidweardocs.EXTRA_VOICE_REPLY";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(this.ACTION_DEMAND)) {

            String message = intent.getStringExtra(this.EXTRA_MESSAGE);
            Log.v("MyTag","Extra message from intent = " + message);
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            CharSequence reply = remoteInput.getCharSequence(this.EXTRA_VOICE_REPLY);
            Log.v("MyTag", "User reply from wearable: " + reply);

            // Broadcast message to wearable activity for display or any other purpose
            String replyString = reply.toString();
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("reply", replyString);
            LocalBroadcastManager.getInstance(context).sendBroadcast(messageIntent);
        }
    }
}


