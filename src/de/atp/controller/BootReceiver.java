package de.atp.controller;

import org.joda.time.DateTime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver
{

@Override
public void onReceive(Context context, Intent intent) {
    if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
        Intent myIntent = new Intent(context, Alarm.class);
        AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);
        DataController controller = DataController.instance();

        if (controller == null) {
            Toast.makeText(context, "Can't load controller!", Toast.LENGTH_LONG).show();
            return;
        }

        DateTime alarmTime = controller.getNextAlarm();

        // Error while founding the alarm time
        if (alarmTime == null) {
            Toast.makeText(context, "No alarm found!", Toast.LENGTH_LONG).show();
            return;
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getMillis(), pendingIntent);
    }
}

}