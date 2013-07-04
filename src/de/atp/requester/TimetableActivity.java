package de.atp.requester;

import de.atp.controller.Alarm;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.atp.controller.DataController;

public class TimetableActivity extends Activity implements OnClickListener {

    private List<RowButton> timeButtons = new ArrayList<RowButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_9am), 0, 9) );        
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_10am), 0, 10) );
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_11am), 0, 11) );
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_12pm), 0, 12) );
        
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_1pm), 1, 13) );
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_2pm), 1, 14) );
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_3pm), 1, 15) );

        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_4pm), 2, 16) );
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_5pm), 2, 17) );
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_6pm), 2, 18) );
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_7pm), 2, 19) );
        
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_8pm), 3, 20) );
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_9pm), 3, 21) );
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_10pm), 3, 22) );
        timeButtons.add( new RowButton( (ToggleButton)findViewById(R.id.button_11pm), 3, 23) );

        for (int i = 0; i < timeButtons.size(); i++) 
            timeButtons.get(i).getTogglebutton().setOnClickListener(this);

        // Create done button
        Button button_done = (Button) findViewById(R.id.button_done);
        button_done.setOnClickListener(this);
    }

    private void setButtonRowToRed(int row) {
        Toast.makeText(this, "ROT!", 1).show();
        for (int i = 0; i < timeButtons.size(); i++) 
            if (timeButtons.get(i).getRow() == row)
                timeButtons.get(i).getTogglebutton().setChecked(false);
    }
    
    private ToggleButton getButtonFromTime(int time) {
        for (int i = 0; i < timeButtons.size(); i++)
            if (timeButtons.get(i).getTime() == time)
                return timeButtons.get(i).getTogglebutton();
        return null;
    }

    private void toggle(int row, int hour) {
        setButtonRowToRed(row);
        getButtonFromTime(hour).toggle();
    }
    /*
    private disableButton() {
        for (int i = 0; timeButtons.size(); i++)
            if (dc.getTodaysAlarms().get(row).isAfter(LocalTime.now()) && 
                    new LocalTime(hour, 0, 0).isBefore(LocalTime.now()))
    }
    */
    
    @Override
    protected void onResume() {
        super.onResume();
        DataController dc = DataController.instance();
        List<LocalTime> alarms = dc.getTodaysAlarms();
        for (int i = 0; i < alarms.size(); i++) {
            setButtonRowToRed(i);
            getButtonFromTime(alarms.get(i).getHourOfDay()).setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //row 1
            case R.id.button_9am:   toggle(0, 9);  break;
            case R.id.button_10am:  toggle(0, 10); break;
            case R.id.button_11am:  toggle(0, 11); break;
            case R.id.button_12pm:  toggle(0, 12); break;
            //row 2
            case R.id.button_1pm:   toggle(1, 13);  break;
            case R.id.button_2pm:   toggle(1, 14);  break;
            case R.id.button_3pm:   toggle(1, 15);  break;
            //row 3
            case R.id.button_4pm:   toggle(2, 16);  break;
            case R.id.button_5pm:   toggle(2, 17);  break;
            case R.id.button_6pm:   toggle(2, 18);  break;
            case R.id.button_7pm:   toggle(2, 19);  break;
            //row 4
            case R.id.button_8pm:   toggle(3, 20);  break;
            case R.id.button_9pm:   toggle(3, 21);  break;
            case R.id.button_10pm:  toggle(3, 22); break;
            case R.id.button_11pm:  toggle(3, 23); break;         

            //@formatter:on
            case R.id.button_done :
                if (!buttonCheck()) {
                    Toast.makeText(this, R.string.timetableToastMessage, Toast.LENGTH_SHORT).show();
                } else {
                    // Persist next alarm times
                    DataController controller = DataController.instance();
                    List<LocalTime> oldAlarms = controller.getTodaysAlarms();
                    int numEntries = 0;
                    for (int i = 0; i < timeButtons.size(); ++i) {
                        // Selected alarm time
                        if (timeButtons.get(i).getTogglebutton().isChecked()) {
                            // First possible time begins at 9 o'clock
                            // and max is 23 (24 = 0)
                            int hour = (i + 9) % 24;
                            if (oldAlarms.isEmpty())
                                controller.createDummyRow(hour, 0);
                            else
                                controller.changeAlarmTime(oldAlarms.get(numEntries).getHourOfDay(), 0, hour, 0);
                            numEntries++;
                            setFirstAlarm();
                        }
                    }
                    startActivity(new Intent(this, InfoActivity.class));
                    finish();
                }
        }
    }

    private boolean buttonCheck() {
        boolean row1 = false;
        boolean row2 = false;
        boolean row3 = false;
        boolean row4 = false;
        
        for (int i = 0; i < timeButtons.size(); i++) {
            if (timeButtons.get(i).getRow() == 0)
                row1 = row1 || timeButtons.get(i).getTogglebutton().isChecked();
            else if (timeButtons.get(i).getRow() == 1)
                row2 = row2 || timeButtons.get(i).getTogglebutton().isChecked();
            else if (timeButtons.get(i).getRow() == 2)
                row3 = row3 || timeButtons.get(i).getTogglebutton().isChecked();
            else if (timeButtons.get(i).getRow() == 3)
                row4 = row4 || timeButtons.get(i).getTogglebutton().isChecked();
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

}
