package de.atp.requester;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.atp.controller.DataController;
import de.atp.date.ATPTime;

public class InfoActivity extends Activity {

    private final static DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());

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
        ATPTime alarmTime = controller.getNextAlarm();
        ATPTime now = new ATPTime();
        String text = "";
        if (alarmTime.before(now)) {
            text = "Morgen um ";
        }
        text += TIME_FORMAT.format(alarmTime.asDate());

        view.setText(text);

        view = (TextView) findViewById(R.id.infoActivity_time);
        view.setText(TIME_FORMAT.format(alarmTime.diff(now).asDate()));

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
