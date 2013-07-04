package de.atp.requester;

import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import de.atp.controller.DataController;

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
            // TODO: Use completly new interface
            maxMinutes = (new Date()).getTime() - DataController.instance().getLastAnsweredDate().toDate().getTime();
            maxMinutes /= 60000;
        } catch (java.lang.NullPointerException e) {
            maxMinutes = Integer.MAX_VALUE;
        }
        try {
            minutes = getValue(R.id.minutes) + 60 * getValue(R.id.hours);
            contacts = getValue(R.id.numberOfContacts);
        } catch (java.lang.NullPointerException e) {
            invalidInput = true;
            toastText(v.getResources().getString(R.string.missingInput));
        }
        if (contacts == 0 ^ minutes == 0)
            toastText(v.getResources().getString(R.string.contactsMinutesMismatch));
        else if (!invalidInput && !checkValue(R.id.minutes, 59))
            toastText(v.getResources().getString(R.string.invalidMinutes));
        else if (minutes > maxMinutes)
            toastText(v.getResources().getString(R.string.tooMuchMinutes));
        else if (!invalidInput) {
            saveValues();
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
