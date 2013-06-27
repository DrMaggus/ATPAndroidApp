package de.atp.requester;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.atp.controller.Alarm;

public class StartActivity extends Activity implements OnClickListener{
    PendingIntent pi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmManager testam1 = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));  ;
        int testhour1 = 1;
        int testminute1 = 0;

        setContentView(R.layout.activity_start);
        Button button = (Button) findViewById(R.id.button1);
        setup();
        setAlarmManager(testhour1, testminute1, testam1);

        button.setOnClickListener((OnClickListener) this);
    }
    /**
     * sets up Intent and PendingIntent
     */
    private void setup() {
        Intent myIntent = new Intent(StartActivity.this, Alarm.class);
        pi = PendingIntent.getService(StartActivity.this, 0, myIntent, 0);

    }
    /**
     * call this method if you want to create a new Alarm
     * @param hour
     * @param minute
     * @param am
     */
    private void setAlarmManager(int hour, int minute, AlarmManager am)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, hour);
        cal.add(Calendar.MINUTE, minute);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi );
    }
    
    private boolean codeIsValid(String c) {
        return c.matches(".*\\d.*") ? false : true;           
    }
    

    public void onClick(View v) {
        String code = ((EditText) findViewById(R.id.editTextCode)).getText().toString();
        if (codeIsValid(code))
            startActivity(new Intent(this, TimetableActivity.class));
        //TODO: abspeichern
    }

}
