package com.androidweardocs.wearabledemand;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationManagerCompat;
import
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static com.androidweardocs.wearabledemand.DemandIntentReceiver.ACTION_DEMAND;
import static com.androidweardocs.wearabledemand.DemandIntentReceiver.EXTRA_VOICE_REPLY;

public class HandheldActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handheld);

        // Create an Intent for the wearable demand
        Intent demandIntent = new Intent(this, DemandIntentReceiver.class).putExtra(DemandIntentReceiver.EXTRA_MESSAGE, "Reply icon selected.").setAction(ACTION_DEMAND);

        // Create a pending intent for the wearable demand to include in the notification
        PendingIntent demandPendingIntent =
                PendingIntent.getBroadcast(this, 0, demandIntent, 0);

        // Create RemoteInput object for a voice reply (demand)
        String replyLabel = getResources().getString(R.string.app_name);
        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel(replyLabel)
                .build();

        // Create a wearable action
        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon,
                        getString(R.string.reply_label), demandPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // Create a Wearable extender and add the action
        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .addAction(replyAction);

        // Create a notification that includes the wearableExtender
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Hello Wearable!")
                        .setContentText("First Wearable demand.")
                        .setSmallIcon(R.drawable.ic_launcher)
                        .extend(wearableExtender)
                        .build();

        // Get an instance of the NotificationManagerCompat
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        //Dispatch the extendable notification
        int notificationId = 1;
        notificationManager.notify(notificationId, notification);

        // Register a message receiver for the users demand.
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).
                registerReceiver(messageReceiver, messageFilter);
    }

    // Class to receive demand text from the wearable demand receiver
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            // Display the received demand

            TextView demandView = (TextView) findViewById(R.id.demand_text);
            String demand = demandView.getText() + "\nDemand from wearable is: " + intent.getStringExtra("reply");
            demandView.setText(demand);
        }
    }
}
