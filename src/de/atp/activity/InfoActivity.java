package de.atp.activity;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.atp.controller.DataController;
import de.atp.requester.R;

public class InfoActivity extends Activity {

    private final static DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm");

    private TextView alarmTimeView;
    private TextView timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Button button = (Button) findViewById(R.id.infoActivity_timetableButton);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openTimetable();
            }
        });

        button = (Button) findViewById(R.id.infoActivity_quitButton);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                closeApp();
            }
        });

        alarmTimeView = (TextView) findViewById(R.id.infoActivity_alarmTime);
        timeView = (TextView) findViewById(R.id.infoActivity_time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataController controller = DataController.instance();
        if (controller == null) {
            Toast.makeText(getBaseContext(), "Can't load controller!", Toast.LENGTH_LONG).show();
            return;
        }
        DateTime alarmTime = controller.getNextAlarm();

        // Error while founding the alarm time
        if (alarmTime == null) {
            Toast.makeText(getBaseContext(), "No alarm found!", Toast.LENGTH_LONG).show();
            timeView.setText("");
            alarmTimeView.setText("");
            return;
        }

        String text = "";
        // Check if the next alarm is tomorrow
        if (alarmTime.getDayOfMonth() != LocalDate.now().getDayOfMonth()) {
            text = "Morgen um ";
        }
        text += TIME_FORMAT.print(new LocalTime(alarmTime));

        alarmTimeView.setText(text);

        Period period = new Period(DateTime.now(), alarmTime);
        LocalTime diff = new LocalTime(period.getHours(), period.getMinutes());
        timeView.setText(TIME_FORMAT.print(diff));
    }

    private void openTimetable() {

        startActivity(new Intent(this, TimetableActivity.class));
        finish();
    }

    private void closeApp() {
        finish();
    }
}
