package de.atp.requester;

import java.util.Calendar;

import de.atp.controller.Alarm;
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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        
        button_9am =  (ToggleButton) findViewById(R.id.button_9am);
        button_10am = (ToggleButton) findViewById(R.id.button_10am);
        button_11am = (ToggleButton) findViewById(R.id.button_11am);
        button_12pm = (ToggleButton) findViewById(R.id.button_12pm);
        button_1pm = (ToggleButton) findViewById(R.id.button_1pm);
        button_2pm = (ToggleButton) findViewById(R.id.button_2pm);
        button_3pm = (ToggleButton) findViewById(R.id.button_3pm);
        button_4pm = (ToggleButton) findViewById(R.id.button_4pm);
        button_5pm = (ToggleButton) findViewById(R.id.button_5pm);
        button_6pm = (ToggleButton) findViewById(R.id.button_6pm);
        button_7pm = (ToggleButton) findViewById(R.id.button_7pm);
        button_8pm = (ToggleButton) findViewById(R.id.button_8pm);
        button_9pm = (ToggleButton) findViewById(R.id.button_9pm);
        button_10pm = (ToggleButton) findViewById(R.id.button_10pm);
        button_11pm = (ToggleButton) findViewById(R.id.button_11pm);
        button_done = (Button) findViewById(R.id.button_done);
        
        button_9am.setOnClickListener(this);
        button_10am.setOnClickListener(this);
        button_11am.setOnClickListener(this);
        button_12pm.setOnClickListener(this);
        button_1pm.setOnClickListener(this);
        button_2pm.setOnClickListener(this);
        button_3pm.setOnClickListener(this);
        button_4pm.setOnClickListener(this);
        button_5pm.setOnClickListener(this);
        button_6pm.setOnClickListener(this);
        button_7pm.setOnClickListener(this);
        button_8pm.setOnClickListener(this);
        button_9pm.setOnClickListener(this);
        button_10pm.setOnClickListener(this);
        button_11pm.setOnClickListener(this);
        
        button_done.setOnClickListener(this);
        
        setup();
        
    }
    
    
    private void setButtonRowToRed(int row) {
        switch(row) {
            case 0:
                ((ToggleButton) findViewById(R.id.button_9am)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_10am)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_11am)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_12pm)).setChecked(false);
                break;
            case 1:
                ((ToggleButton) findViewById(R.id.button_1pm)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_2pm)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_3pm)).setChecked(false);
                break;
            case 2:
                ((ToggleButton) findViewById(R.id.button_4pm)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_5pm)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_6pm)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_7pm)).setChecked(false);
                break;
            case 3:
                ((ToggleButton) findViewById(R.id.button_8pm)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_9pm)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_10pm)).setChecked(false);
                ((ToggleButton) findViewById(R.id.button_11pm)).setChecked(false);
                break;
        }
    }
    
    private void toggle(int row, int id) {
        setButtonRowToRed(row); 
        ((ToggleButton) findViewById(id)).setChecked(true);
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
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
            
            case R.id.button_done: 
                if(!buttonCheck()){
                    Toast.makeText(this, R.string.timetableToastMessage, Toast.LENGTH_SHORT).show();                    
                } else {
                    for (int i=0; i<4; i++)
                        setAlarmManager(hour[i], am[i]);
                    moveTaskToBack(true);
                }
        }
    }
    
    private boolean buttonCheck(){
        boolean row1;
        boolean row2;
        boolean row3;
        boolean row4;

        row1=((ToggleButton)findViewById(R.id.button_9am)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_10am)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_11am)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_12pm)).isChecked();

        row2=((ToggleButton)findViewById(R.id.button_1pm)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_2pm)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_3pm)).isChecked();


        row3=((ToggleButton)findViewById(R.id.button_4pm)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_5pm)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_6pm)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_7pm)).isChecked();


        row4=((ToggleButton)findViewById(R.id.button_8pm)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_9pm)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_10pm)).isChecked()||
                ((ToggleButton)findViewById(R.id.button_11pm)).isChecked();

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

