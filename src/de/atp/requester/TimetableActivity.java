package de.atp.requester;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.atp.controller.DataController;

public class TimetableActivity extends Activity implements OnClickListener {

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
    }

    private void setButtonRowToRed(int row) {
        switch (row) {
            case 0 :
                for (int i = 0; i < 4; ++i)
                    timeButtons.get(i).setChecked(false);
                break;
            case 1 :
                for (int i = 4; i < 7; ++i)
                    timeButtons.get(i).setChecked(false);
                break;
            case 2 :
                for (int i = 7; i < 11; ++i)
                    timeButtons.get(i).setChecked(false);
                break;
            case 3 :
                for (int i = 11; i < 15; ++i)
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
            case R.id.button_9am:   toggle(0,R.id.button_9am);  break;
            case R.id.button_10am:  toggle(0,R.id.button_10am); break;
            case R.id.button_11am:  toggle(0,R.id.button_11am); break;
            case R.id.button_12pm:  toggle(0,R.id.button_12pm); break;
            //row 2
            case R.id.button_1pm:   toggle(1,R.id.button_1pm);  break;
            case R.id.button_2pm:   toggle(1,R.id.button_2pm);  break;
            case R.id.button_3pm:   toggle(1,R.id.button_3pm);  break;
            //row 3
            case R.id.button_4pm:   toggle(2,R.id.button_4pm);  break;
            case R.id.button_5pm:   toggle(2,R.id.button_5pm);  break;
            case R.id.button_6pm:   toggle(2,R.id.button_6pm);  break;
            case R.id.button_7pm:   toggle(2,R.id.button_7pm);  break;
            //row 4
            case R.id.button_8pm:   toggle(3,R.id.button_8pm);  break;
            case R.id.button_9pm:   toggle(3,R.id.button_9pm);  break;
            case R.id.button_10pm:  toggle(3,R.id.button_10pm); break;
            case R.id.button_11pm:  toggle(3,R.id.button_11pm); break;
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
        for (; i < 4; ++i)
            row1 = row1 || timeButtons.get(i).isChecked();
        for (; i < 7; ++i)
            row2 = row2 || timeButtons.get(i).isChecked();
        for (; i < 11; ++i)
            row3 = row3 || timeButtons.get(i).isChecked();
        for (; i < 15; ++i)
            row4 = row4 || timeButtons.get(i).isChecked();

        return row1 && row2 && row3 && row4;
    }

}
