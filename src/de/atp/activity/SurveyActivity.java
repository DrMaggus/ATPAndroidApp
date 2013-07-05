package de.atp.activity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.atp.controller.Alarm;
import de.atp.controller.DataController;
import de.atp.requester.R;

public class SurveyActivity extends Activity {
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent myIntent;
    @Override
    // this is the worst function I've written in years...
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        registerReceiver(FinishHim, new IntentFilter("finishActivity"));
        setAlarmManager();
        contactQuestion();
        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new SurveyOnClickListener(this));
    }

    private void contactQuestion() {
        DataController dataController = DataController.instance();
        if(dataController==null) {
            errorToast();
            return;
        }
        DateTime date = DataController.instance().getLastAnsweredDate();
        if(date==null) {
            errorToast();
            return;
        }
        DateTimeFormatter dateFormat = DateTimeFormat.forPattern(this.getResources().getString(R.string.surveyActivity_lastAnsweredDate));
        DateTimeFormatter hourFormat = DateTimeFormat.forPattern(this.getResources().getString(R.string.surveyActivity_lastAnsweredTime));
        String contactQuestion = this.getResources().getString(R.string.surveyActivity_contactQuestion, dateFormat.print(date), hourFormat.print(date));
        TextView question = (TextView) findViewById(R.id.contactQuestionView);
        question.setText(contactQuestion);
    }
    private void errorToast() {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, this.getResources().getString(R.string.general_databaseError), duration);
        toast.show();
    }

    /**
     * sets Intent, PendingIntent and the AlarmManager
     */
    private void setup() {
        if (myIntent == null)
            myIntent = new Intent(this, Alarm.class);
        if(alarmManager == null)
            alarmManager=(AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
        if(pendingIntent == null)
            pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
    }

    /**
     * call this method if you want to create a new Alarm
     */
    private void setAlarmManager(){
        setup();
        DataController controller = DataController.instance();
        
        if (controller == null) {
            Toast.makeText(getBaseContext(), "Can't load controller!", Toast.LENGTH_LONG).show();
            return;
        }
       
        DateTime alarmTime = controller.getNextAlarm();

        // Error while founding the alarm time
        if (alarmTime == null) {
            Toast.makeText(getBaseContext(), "No alarm found!", Toast.LENGTH_LONG).show();
            return;
        }
        
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getMillis(), pendingIntent);
        
    }
    
    /**
     * BroadcastReceiver to finish the old SurveyActivities, if already a new
     * one starts
     */
    private final BroadcastReceiver FinishHim = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
}