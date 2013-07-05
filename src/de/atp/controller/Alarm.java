package de.atp.controller;

import de.atp.activity.SurveyActivity;
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
        // close open Activities
        sendBroadcast(new Intent("finishActivity"));
        startSurvey();
        vibration();
        playRingtone();
        Toast.makeText(this, "Bitte Werte eintragen!", Toast.LENGTH_LONG).show();
    }
    
    /**
     * let the smartphone vibrate
     */
    private void vibration(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);        
        vibrator.vibrate(2000);
    }
    
    /**
     * play some Sounds
     */
    private void playRingtone(){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(this, notification);
        ringtone.play();        
    }
    
    /**
     * starts the SurveyActivity
     */
    private void startSurvey(){
        Intent intent = new Intent("android.intent.category.LAUNCHER");
        intent.setClassName(SurveyActivity.class.getPackage().getName(), SurveyActivity.class.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);        
    }

    /**
     * has to be implemented in services
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
