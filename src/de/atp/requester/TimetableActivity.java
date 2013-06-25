package de.atp.requester;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
            case R.id.button_9am: toggle(0,R.id.button_9am); break;
            case R.id.button_10am: toggle(0,R.id.button_10am); break;
            case R.id.button_11am: toggle(0,R.id.button_11am); break;
            case R.id.button_12pm: toggle(0,R.id.button_12pm); break;
            //row 2
            case R.id.button_1pm: toggle(1,R.id.button_1pm); break;
            case R.id.button_2pm: toggle(1,R.id.button_2pm); break;
            case R.id.button_3pm: toggle(1,R.id.button_3pm); break;
            //row 3
            case R.id.button_4pm: toggle(2,R.id.button_4pm); break;
            case R.id.button_5pm: toggle(2,R.id.button_5pm); break;
            case R.id.button_6pm: toggle(2,R.id.button_6pm); break;
            case R.id.button_7pm: toggle(2,R.id.button_7pm); break;
            //row 4
            case R.id.button_8pm: toggle(3,R.id.button_8pm); break;
            case R.id.button_9pm: toggle(3,R.id.button_9pm); break;
            case R.id.button_10pm: toggle(3,R.id.button_10pm); break;
            case R.id.button_11pm: toggle(3,R.id.button_11pm); break;
        }
    }

}
