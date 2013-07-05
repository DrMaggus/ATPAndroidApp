package de.atp.activity;

import org.joda.time.DateTime;
import org.joda.time.Duration;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.atp.controller.Alarm;
import de.atp.controller.DataController;
import de.atp.activity.R;

public class SurveyActivity extends Activity {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new SurveyOnClickListener(this));

        registerReceiver(FinishHim, new IntentFilter("finishActivity"));
        setAlarmManager();
        contactQuestion();
    }

    private void contactQuestion() {
        DataController dataController = DataController.instance();
        if (dataController == null) {
            errorToast();
            return;
        }
        DateTime date = DataController.instance().getLastAnsweredDate();
        if (date == null) {
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
        if (alarmManager == null)
            alarmManager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
        if (pendingIntent == null)
            pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
    }

    /**
     * call this method if you want to create a new Alarm
     */
    private void setAlarmManager() {
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

    private class SurveyOnClickListener implements OnClickListener {
        private SurveyActivity activity;

        public SurveyOnClickListener(SurveyActivity activity) {
            this.activity = activity;
        }

        public void onClick(View v) {
            boolean invalidInput = false;
            long maxMinutes;
            long minutes = 0, contacts = 0;
            try {
                Duration duration = new Duration(DataController.instance().getLastAnsweredDate(), DateTime.now());
                maxMinutes = duration.getStandardMinutes();
            } catch (java.lang.NullPointerException e) {
                maxMinutes = Integer.MAX_VALUE;
            }
            try {
                minutes = getValue(R.id.minutes) + 60 * getValue(R.id.hours);
                contacts = getValue(R.id.numberOfContacts);
            } catch (java.lang.NullPointerException e) {
                invalidInput = true;
                toastText(v.getResources().getString(R.string.surveyActivity_missingInput));
            }
            if (contacts == 0 ^ minutes == 0)
                toastText(v.getResources().getString(R.string.surveyActivity_contactsMinutesMismatch));
            else if (!invalidInput && !checkValue(R.id.minutes, 59))
                toastText(v.getResources().getString(R.string.surveyActivity_invalidMinutes));
            else if (minutes > maxMinutes)
                toastText(v.getResources().getString(R.string.surveyActivity_tooMuchMinutes));
            else if (!invalidInput) {
                saveValues();
                activity.startActivity(new Intent(activity, InfoActivity.class));
                activity.finish();
                return;
            }
        }

        private Integer getValue(int id) {
            EditText editText = (EditText) activity.findViewById(id);
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
            Context context = activity.getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        private void saveValues() {
            DataController.instance().completeQuestions(getValue(R.id.hours), getValue(R.id.minutes), getValue(R.id.numberOfContacts));
        }
    }
}