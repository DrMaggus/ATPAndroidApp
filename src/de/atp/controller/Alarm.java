package de.atp.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class Alarm extends Service{    

    /**
     * simply the super destructor
     */
    @Override
    public void onDestroy() {
    super.onDestroy();

    Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();


    }

    /**
     * let the App appear on alarm.
     * @param: intent is the intent, which calls the method; 
     * @param startId;
     */
    @Override
    public void onStart(Intent intent, int startId) {
    
    Intent test = new Intent("android.intent.category.LAUNCHER");
    test.setClassName("de.atp.requester", "de.atp.requester.StartActivity");
    test.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(test);
    Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
    this.stopSelf();
    }


/**
 * has to be implemented.
 * TODO: find out what this function is doing.
 */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
}
