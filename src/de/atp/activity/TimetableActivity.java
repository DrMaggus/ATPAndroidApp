package de.atp.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.atp.controller.Alarm;
import de.atp.controller.DataController;
import de.atp.activity.R;

public class TimetableActivity extends Activity {

    private List<RowButton> timeButtons = new ArrayList<RowButton>();
    private Map<LocalTime, RowButton> buttonsByTime = new HashMap<LocalTime, RowButton>();
    private SparseArray<RowButton> buttonsById = new SparseArray<RowButton>();

    private Button button_done;
    private Button button_cancel;

    private final static int ROW_1 = 0;
    private final static int ROW_2 = 1;
    private final static int ROW_3 = 2;
    private final static int ROW_4 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_9am), ROW_1, new LocalTime(9, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_10am), ROW_1, new LocalTime(10, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_11am), ROW_1, new LocalTime(11, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_12pm), ROW_1, new LocalTime(12, 00)));

        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_1pm), ROW_2, new LocalTime(13, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_2pm), ROW_2, new LocalTime(14, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_3pm), ROW_2, new LocalTime(15, 00)));

        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_4pm), ROW_3, new LocalTime(16, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_5pm), ROW_3, new LocalTime(17, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_6pm), ROW_3, new LocalTime(18, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_7pm), ROW_3, new LocalTime(19, 00)));

        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_8pm), ROW_4, new LocalTime(20, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_9pm), ROW_4, new LocalTime(21, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_10pm), ROW_4, new LocalTime(22, 00)));
        timeButtons.add(new RowButton((ToggleButton) findViewById(R.id.button_11pm), ROW_4, new LocalTime(23, 00)));

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                handleTimeButtonClick(buttonsById.get(v.getId()));
            }
        };

        for (RowButton button : timeButtons) {
            button.togglebutton.setOnClickListener(listener);
            this.buttonsByTime.put(button.time, button);
            this.buttonsById.put(button.togglebutton.getId(), button);
        }

        // Create done button
        button_done = (Button) findViewById(R.id.button_done);
        button_done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoneButtonClick();
            }
        });

        // Create Cancel button
        button_cancel = (Button) findViewById(R.id.timetable_cancel);
        button_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCancelButtonClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataController dc = DataController.instance();
        List<LocalTime> alarms = dc.getTodaysAlarms();
        for (int i = 0; i < alarms.size(); i++) {
            setButtonRowToRed(i);
            buttonsByTime.get(alarms.get(i)).togglebutton.setChecked(true);
        }
    }

    private void handleTimeButtonClick(RowButton button) {
        toggle(button.row, button.time.getHourOfDay());
    }

    private void handleDoneButtonClick() {
        if (!buttonCheck()) {
            Toast.makeText(this, R.string.timetableToastMessage, Toast.LENGTH_SHORT).show();
        } else {
            // Persist next alarm times
            DataController controller = DataController.instance();
            List<LocalTime> oldAlarms = controller.getTodaysAlarms();
            // TODO: check => sometimes times don't get saved
            // (randomly?!) <=> csv gets fucked up
            for (int i = 0, numEntries = 0; i < timeButtons.size(); i++) {
                if (timeButtons.get(i).togglebutton.isChecked()) {
                    if (oldAlarms.isEmpty())
                        controller.createFirstAlarms(timeButtons.get(i).time.getHourOfDay(), timeButtons.get(i).time.getMinuteOfHour());
                    else if (timeChoiceIsValid(oldAlarms.get(numEntries).getHourOfDay(), timeButtons.get(i).time.getHourOfDay()))
                        controller.changeAlarmTime(oldAlarms.get(numEntries).getHourOfDay(), 0, timeButtons.get(i).time.getHourOfDay(), 0);
                    else
                        Toast.makeText(this, "Zeiten wurden so gewaehlt, dass sie heute nicht mehr erreichbar bzw. wieder erreichbar sind", Toast.LENGTH_LONG).show();
                    numEntries++;
                }
            }
            setFirstAlarm();
            startActivity(new Intent(this, InfoActivity.class));
            finish();
        }

    }

    private void handleCancelButtonClick() {
        finish();
    }

    private void setButtonRowToRed(int row) {
        for (int i = 0; i < timeButtons.size(); i++)
            if (timeButtons.get(i).row == row)
                timeButtons.get(i).togglebutton.setChecked(false);
    }

    private void toggle(int row, int hour) {
        setButtonRowToRed(row);
        buttonsByTime.get(new LocalTime(hour, 0)).togglebutton.setChecked(true);
    }

    private boolean timeChoiceIsValid(int oldTime, int newTime) {
        DataController dc = DataController.instance();
        if (dc == null) {
            Toast.makeText(this, "Controller Error", Toast.LENGTH_LONG).show();
            return false;
        }

        List<LocalTime> todayAlarms = dc.getTodaysAlarms();
        if (todayAlarms.isEmpty())
            return true;

        LocalTime alarmTime = new LocalTime(newTime, 0);
        int row = buttonsByTime.get(new LocalTime(oldTime, 0)).row;
        LocalTime rowTime = todayAlarms.get(row);
        LocalTime now = LocalTime.now();

        return !((rowTime.isAfter(now) && alarmTime.isBefore(now)) || rowTime.isBefore(now) && alarmTime.isAfter(now));
    }

    private boolean buttonCheck() {
        boolean row1 = false;
        boolean row2 = false;
        boolean row3 = false;
        boolean row4 = false;

        for (int i = 0; i < timeButtons.size(); i++) {
            if (timeButtons.get(i).row == ROW_1)
                row1 = row1 || timeButtons.get(i).togglebutton.isChecked();
            else if (timeButtons.get(i).row == ROW_2)
                row2 = row2 || timeButtons.get(i).togglebutton.isChecked();
            else if (timeButtons.get(i).row == ROW_3)
                row3 = row3 || timeButtons.get(i).togglebutton.isChecked();
            else if (timeButtons.get(i).row == ROW_4)
                row4 = row4 || timeButtons.get(i).togglebutton.isChecked();
        }

        return row1 && row2 && row3 && row4;
    }

    /**
     * call this method to set first Alarm
     */
    private void setFirstAlarm() {
        Intent myIntent = new Intent(this, Alarm.class);
        AlarmManager alarmManager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
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

    private class RowButton {
        private final ToggleButton togglebutton;
        private final int row;
        private final LocalTime time;

        public RowButton(ToggleButton togglebutton, int row, LocalTime time) {
            this.togglebutton = togglebutton;
            this.row = row;
            this.time = time;
        }
    }

}
