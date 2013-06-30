package de.atp.controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

public class Alarm extends Service {

    /**
     * simply the super destructor
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

//        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();

    }

    /**
     * let the App appear on alarm.
     * 
     * @param: intent is the intent, which calls the method;
     * @param startId;
     */
    @Override
    public void onStart(Intent intent, int startId) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Intent test = new Intent("android.intent.category.LAUNCHER");
        test.setClassName("de.atp.requester", "de.atp.requester.SurveyActivity");
        test.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(test);
        vibrator.vibrate(2000);
       // Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri test1 = getAlarmUri();
        Ringtone r = RingtoneManager.getRingtone(this, test1);
        r.play();
        Toast.makeText(this, "Bitte ausfüllen!", 3).show();
//        this.stopSelf();
    }
    
    /**
     * @return the Uri of a ringtone
     */
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
    
    /**
     * has to be implemented. TODO: find out what this function is doing.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
