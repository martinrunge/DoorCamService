package de.rungenetz.doorcamservice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class hello extends AppCompatActivity {

    Button button = null;
    Button startServiceBtn = null;
    TextView tv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        button = findViewById(R.id.button);
        startServiceBtn = findViewById(R.id.start_service_btn);
        tv = findViewById(R.id.label_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isMyServiceRunning(DoorCamService.class)) {
                    tv.setText("Service is runnng !");
                }
                else {
                    tv.setText("Service not runnng");
                }
            }
        });

        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(hello.super.getBaseContext(), DoorCamService.class);
                startService(intent);
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
