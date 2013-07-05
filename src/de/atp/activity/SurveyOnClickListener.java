package de.atp.activity;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import de.atp.controller.DataController;
import de.atp.requester.R;

public class SurveyOnClickListener implements OnClickListener {
    private SurveyActivity activity;
    
    public SurveyOnClickListener(SurveyActivity activity) {
        this.activity = activity;
    }
    
    public void onClick(View v) {
        boolean invalidInput = false;
        long maxMinutes;
        long minutes = 0, contacts = 0;
        try {
            Duration duration = new Duration(DataController.instance().getLastAnsweredDate(), DateTime.now());
            maxMinutes = duration.getStandardMinutes();
        } catch (java.lang.NullPointerException e) {
            maxMinutes = Integer.MAX_VALUE;
        }
        try {
            minutes = getValue(R.id.minutes) + 60 * getValue(R.id.hours);
            contacts = getValue(R.id.numberOfContacts);
        } catch (java.lang.NullPointerException e) {
            invalidInput = true;
            toastText(v.getResources().getString(R.string.surveyActivity_missingInput));
        }
        if (contacts == 0 ^ minutes == 0)
            toastText(v.getResources().getString(R.string.surveyActivity_contactsMinutesMismatch));
        else if (!invalidInput && !checkValue(R.id.minutes, 59))
            toastText(v.getResources().getString(R.string.surveyActivity_invalidMinutes));
        else if (minutes > maxMinutes)
            toastText(v.getResources().getString(R.string.surveyActivity_tooMuchMinutes));
        else if (!invalidInput) {
            saveValues();
            activity.startActivity(new Intent(activity, InfoActivity.class));
            activity.finish();
            return;
        }
    }

    private Integer getValue(int id) {
        EditText editText = (EditText)activity.findViewById(id);
        String text = editText.getText().toString();
        try {
            return Integer.parseInt(text);
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    private boolean checkValue(int id, int limit) {
        try {
            return getValue(id) <= limit;
        } catch (java.lang.NullPointerException e) {
            return false;
        }
    }

    private void toastText(CharSequence text) {
        Context context = activity.getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void saveValues() {
        DataController.instance().completeQuestions(getValue(R.id.hours), getValue(R.id.minutes), getValue(R.id.numberOfContacts));
    }
}