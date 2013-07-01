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
    }

    /**
     * Let the SurveyActivity appear on alarm.
     * 
     * @param: intent , which calls the launcher-method;
     * @param: startId;
     */
    @Override
    public void onStart(Intent intent, int startId) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sendBroadcast(new Intent("finishActivity"));
        Intent test = new Intent("android.intent.category.LAUNCHER");
        test.setClassName("de.atp.requester", "de.atp.requester.SurveyActivity");
        test.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(test);
        vibrator.vibrate(2000);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(this, notification);
        ringtone.play();
        Toast.makeText(this, "Bitte Werte eintragen!", 3).show();
    }

    /**
     * has to be implemented.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
