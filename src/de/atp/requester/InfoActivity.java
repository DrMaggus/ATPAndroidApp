package de.atp.requester;

import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.atp.controller.DataController;

public class InfoActivity extends Activity {

    private final static DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        DataController controller = DataController.instance();
        if (controller == null) {
            Toast.makeText(getBaseContext(), "Can't load controller!", Toast.LENGTH_LONG).show();
            return;
        }
        TextView view = (TextView) findViewById(R.id.infoActivity_alarmTime);
        LocalTime alarmTime = controller.getNextAlarm();
        LocalTime now = new LocalTime();
        String text = "";
        if (alarmTime.isBefore(now)) {
            text = "Morgen um ";
        }
        text += TIME_FORMAT.print(alarmTime);

        view.setText(text);

        view = (TextView) findViewById(R.id.infoActivity_time);
        Period period = new Period(alarmTime, now);
        LocalTime diff = new LocalTime(period.getHours(), period.getMinutes());
        view.setText(TIME_FORMAT.print(diff));

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
    }

    private void openTimetable() {
        // TODO: Open timetable
    }

    private void closeApp() {
        // TODO: Close app
    }
}
