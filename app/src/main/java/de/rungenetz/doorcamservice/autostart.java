package de.rungenetz.doorcamservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class autostart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context,DoorCamService.class);
        context.startService(intent);
        final int i = Log.i("Autostart", "started");
    }
}
