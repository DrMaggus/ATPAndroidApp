package de.atp.requester;


import java.util.Calendar;

import de.atp.controller.Alarm;

import java.util.ArrayList;
import java.util.List;


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


    ToggleButton button_9am;
    ToggleButton button_10am;
    ToggleButton button_11am;
    ToggleButton button_12pm;
    ToggleButton button_1pm;
    ToggleButton button_2pm;
    ToggleButton button_3pm;
    ToggleButton button_4pm;  
    ToggleButton button_5pm;
    ToggleButton button_6pm;
    ToggleButton button_7pm;
    ToggleButton button_8pm;
    ToggleButton button_9pm;
    ToggleButton button_10pm;
    ToggleButton button_11pm;
    Button button_done;
    PendingIntent pi;
    int[] hour = new int[4];
    AlarmManager[] am = new AlarmManager[4];
    

    // Row indices
    private static final int ROW_1 = 4;
    private static final int ROW_2 = 7;
    private static final int ROW_3 = 11;
    private static final int ROW_4 = 15;

    private List<ToggleButton> timeButtons = new ArrayList<ToggleButton>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        // Create toggle buttons
        ToggleButton timeButton;

        timeButton = (ToggleButton) findViewById(R.id.button_9am);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_10am);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_11am);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_12pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_1pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_2pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_3pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_4pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_5pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_6pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_7pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_8pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_9pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_10pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        timeButton = (ToggleButton) findViewById(R.id.button_11pm);
        timeButton.setOnClickListener(this);
        timeButtons.add(timeButton);

        // Create done button
        Button button_done = (Button) findViewById(R.id.button_done);
        button_done.setOnClickListener(this);
        
        setup();

    }

    private void setButtonRowToRed(int row) {
        switch (row) {
            case 0 :
                for (int i = 0; i < ROW_1; ++i)
                    timeButtons.get(i).setChecked(false);
                break;
            case 1 :
                for (int i = ROW_1; i < ROW_2; ++i)
                    timeButtons.get(i).setChecked(false);
                break;
            case 2 :
                for (int i = ROW_2; i < ROW_3; ++i)
                    timeButtons.get(i).setChecked(false);
                break;
            case 3 :
                for (int i = ROW_3; i < ROW_4; ++i)
                    timeButtons.get(i).setChecked(false);
                break;
        }
    }

    private void toggle(int row, int id) {
        setButtonRowToRed(row);
        ((ToggleButton) findViewById(id)).setChecked(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        //@formatter:off
            //row 1
            case R.id.button_9am: {
                toggle(0,R.id.button_9am); 
                hour[0]=9;
                break;
            }
            case R.id.button_10am: {
                toggle(0,R.id.button_10am); 
                hour[0]=10;
                break;
            }
            case R.id.button_11am: {
                toggle(0,R.id.button_11am); 
                hour[0]=11;
                break;
            }
            case R.id.button_12pm: {
                toggle(0,R.id.button_12pm); 
                hour[0]=12;
                break;
            }
            //row 2
            case R.id.button_1pm: {
                toggle(1,R.id.button_1pm); 
                hour[1]=13;
                break;
            }
            case R.id.button_2pm: {
                toggle(1,R.id.button_2pm); 
                hour[1]=14;
                break;
            }
            case R.id.button_3pm: {
                toggle(1,R.id.button_3pm); 
                hour[1]=15;
                break;
            }
            //row 3
            case R.id.button_4pm: {
                toggle(2,R.id.button_4pm); 
                hour[2]=16;
                break;
            }
            case R.id.button_5pm: {
                toggle(2,R.id.button_5pm); 
                hour[2]=17;
                break;
            }
            case R.id.button_6pm: {
                toggle(2,R.id.button_6pm); 
                hour[2]=18;
                break;
            }
            case R.id.button_7pm: {
                toggle(2,R.id.button_7pm); 
                hour[2]=19;
                break;
            }
            //row 4
            case R.id.button_8pm: {
                toggle(3,R.id.button_8pm); 
                hour[3]=20;
                break;
            }
            case R.id.button_9pm: {
                toggle(3,R.id.button_9pm); 
                hour[3]=21;
                break;
            }
            case R.id.button_10pm: {
                toggle(3,R.id.button_10pm); 
                hour[3]=22;
                break;
            }
            case R.id.button_11pm: {
                toggle(3,R.id.button_11pm); 
                hour[3]=23;
                break;
            }            

            //@formatter:on
            case R.id.button_done :
                if (!buttonCheck()) {
                    Toast.makeText(this, R.string.timetableToastMessage, Toast.LENGTH_SHORT).show();
                } else {
                    // Persist next alarm times
                    DataController controller = DataController.instance();
                    for (int i = 0; i < timeButtons.size(); ++i) {
                        // Selected alarm time
                        if (timeButtons.get(i).isChecked()) {
                            // First possible time begins at 9 o'clock
                            // and max is 23 (24 = 0)
                            int hour = (i + 9) % 24;
                            // save alarm time
                            setAlarmManager(hour, am[i]);
                            controller.createDummyRow(hour, 0);
                        }
                    }
                    moveTaskToBack(true);
                }
        }
    }

    private boolean buttonCheck() {
        boolean row1 = false;
        boolean row2 = false;
        boolean row3 = false;
        boolean row4 = false;

        int i = 0;
        for (; i < ROW_1; ++i)
            row1 = row1 || timeButtons.get(i).isChecked();
        for (; i < ROW_2; ++i)
            row2 = row2 || timeButtons.get(i).isChecked();
        for (; i < ROW_3; ++i)
            row3 = row3 || timeButtons.get(i).isChecked();
        for (; i < ROW_4; ++i)
            row4 = row4 || timeButtons.get(i).isChecked();

        return row1&&row2&&row3&&row4;

    }

    /**
     * sets up Intent and PendingIntent
     */
    private void setup() {
        Intent myIntent = new Intent(this, Alarm.class);
        pi = PendingIntent.getService(this, 0, myIntent, 0);
        for(int i=0; i<4; i++)
            am[i]=(AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }

    /**
     * call this method if you want to create a new Alarm
     * @param hour
     * @param minute
     * @param am
     */
    private void setAlarmManager(int hour, AlarmManager am)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, hour);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi ); 
    }

}
