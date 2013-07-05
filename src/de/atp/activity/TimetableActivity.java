package de.atp.activity;

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
import de.atp.requester.R;

public class TimetableActivity extends Activity implements OnClickListener {

    private List<RowButton> timeButtons = new ArrayList<RowButton>();
    private Button button_done;
    private Button button_cancel;

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
        button_done = (Button) findViewById(R.id.button_done);
        button_done.setOnClickListener(this);
        //Create Cancel button
        button_cancel = (Button) findViewById(R.id.timetable_cancel);
        button_cancel.setOnClickListener(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        DataController dc = DataController.instance();
        List<LocalTime> alarms = dc.getTodaysAlarms();
        for (int i = 0; i < alarms.size(); i++) {
            setButtonRowToRed(i);
            getButtonFromTime(alarms.get(i).getHourOfDay()).getTogglebutton().setChecked(true);
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

            case R.id.timetable_cancel: finish(); break;

            case R.id.button_done :
                if (!buttonCheck()) {
                    Toast.makeText(this, R.string.timetableToastMessage, Toast.LENGTH_SHORT).show();
                } else {
                    // Persist next alarm times
                    DataController controller = DataController.instance();
                    List<LocalTime> oldAlarms = controller.getTodaysAlarms();
                  //TODO: check => sometimes times don't get saved (randomly?!) <=> csv gets fucked up
                    for (int i = 0,numEntries = 0; i < timeButtons.size(); i++) {
                        if(timeButtons.get(i).getTogglebutton().isChecked()) {
                            if (oldAlarms.isEmpty())
                                controller.createDummyRow( timeButtons.get(i).getTime() , 0);
                            else 
                                if (timeChoiceIsValid( oldAlarms.get(numEntries).getHourOfDay() , timeButtons.get(i).getTime() ))
                                    controller.changeAlarmTime( oldAlarms.get(numEntries).getHourOfDay(), 0, timeButtons.get(i).getTime(), 0);
                                else
                                    Toast.makeText(this, "Zeiten wurden so gewaehlt, dass sie heute nicht mehr erreichbar bzw. wieder erreichbar sind", 6).show();
                                numEntries++;
                            }
                    }
                    setFirstAlarm();
                    startActivity(new Intent(this, InfoActivity.class));
                    finish();
                }
                
        }
    }

    private RowButton getButtonFromTime(int time) {
        for (int i = 0; i < timeButtons.size(); i++)
            if (timeButtons.get(i).getTime() == time)
                return timeButtons.get(i);
        return null;
    }
    
    private void setButtonRowToRed(int row) {
        for (int i = 0; i < timeButtons.size(); i++) 
            if (timeButtons.get(i).getRow() == row)
                timeButtons.get(i).getTogglebutton().setChecked(false);
    }

    private void toggle(int row, int hour) {
        setButtonRowToRed(row);
        getButtonFromTime(hour).getTogglebutton().setChecked(true);
    }
    
    private boolean timeChoiceIsValid(int oldTime, int newTime) {
        DataController dc = DataController.instance();
        if (!dc.getTodaysAlarms().isEmpty()) {
            if (dc.getTodaysAlarms().get( getButtonFromTime( oldTime ).getRow() ).isAfter( LocalTime.now() ) &&
                    new LocalTime( newTime, 0, 0).isBefore( LocalTime.now() ) )
                return false;
            if (dc.getTodaysAlarms().get( getButtonFromTime( oldTime ).getRow() ).isBefore( LocalTime.now() ) &&
                    new LocalTime( newTime, 0, 0).isAfter( LocalTime.now() ) )
                return false;
        }
        return true;
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
