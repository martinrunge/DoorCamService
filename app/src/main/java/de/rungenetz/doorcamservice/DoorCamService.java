package de.rungenetz.doorcamservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.nanohttpd.protocols.http.*;
import org.nanohttpd.util.ServerRunner;

import java.io.IOException;

public class DoorCamService extends Service {
    private static final String TAG = "Doorbird Service";
    private int startid = 0;
    private HttpServer http_srv = null;

    public DoorCamService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy() {
        if(http_srv != null) {
            http_srv.stop();
        }
        Toast.makeText(this, "Doorbird Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }


    @Override
    public int onStartCommand(Intent intent, int flags,  int startid)
    {
        this.startid = startid;

        try {
            http_srv = new HttpServer(8080, this);
        } catch (IOException ioex) {
            Log.d(TAG, "Failed to create HttpServer: " + ioex.toString());
        }

        Toast.makeText(this, "Doorbird Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");
        return START_STICKY;
    }

}
