package de.atp.requester;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.atp.controller.Alarm;
import de.atp.controller.DataController;

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
        sendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean invalidInput = false;
                long maxMinutes;
                long minutes = 0, contacts = 0;
                try {
                    // TODO: Use completly new interface
                    maxMinutes = (new Date()).getTime() - DataController.instance().getLastAnsweredDate().toDate().getTime();
                    maxMinutes /= 60000;
                } catch (java.lang.NullPointerException e) {
                    maxMinutes = Integer.MAX_VALUE;
                }
                try {
                    minutes = getValue(R.id.minutes) + 60 * getValue(R.id.hours);
                    contacts = getValue(R.id.numberOfContacts);
                } catch (java.lang.NullPointerException e) {
                    invalidInput = true;
                    toastText(v.getResources().getString(R.string.missingInput));
                }
                if (contacts == 0 ^ minutes == 0)
                    toastText(v.getResources().getString(R.string.contactsMinutesMismatch));
                else if (!invalidInput && !checkValue(R.id.minutes, 59))
                    toastText(v.getResources().getString(R.string.invalidMinutes));
                else if (minutes > maxMinutes)
                    toastText(v.getResources().getString(R.string.tooMuchMinutes));
                else if (!invalidInput) {
                    saveValues();
                    return;
                }
            }

            private Integer getValue(int id) {
                EditText editText = (EditText) findViewById(id);
                String text = editText.getText().toString();
                try {
                    return Integer.parseInt(text);
                } catch (java.lang.NumberFormatException e) {
                    return null;
                }
            }

            private boolean checkValue(int id, int limit) {
                try {
                    return getValue(id) <= limit;
                } catch (java.lang.NullPointerException e) {
                    return false;
                }
            }

            private void toastText(CharSequence text) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            private void saveValues() {
                DataController.instance().completeQuestions(getValue(R.id.hours), getValue(R.id.minutes), getValue(R.id.numberOfContacts));
            }
        });
    }

    private void contactQuestion() {
        Calendar cal = GregorianCalendar.getInstance();
        Date date;
        // TODO: Use completly new interface
        date = DataController.instance().getLastAnsweredDate().toDate();
//        date = DataController.instance().getLastAnsweredDate().;
        if (date == null) {
            errorToast();
        }
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String contactQuestion = this.getResources().getString(R.string.contact_question, day, month, hour);
        TextView question = (TextView) findViewById(R.id.contactQuestionView);
        question.setText(contactQuestion);
    }
    private void errorToast() {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, this.getResources().getString(R.string.databaseError), duration);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
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