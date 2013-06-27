package de.atp.requester;

import java.util.Date;

import de.atp.controller.DataController;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SurveyActivity extends Activity {

    @Override
    // this is the worst function I've written in years...
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        String contactQuestion = this.getResources().getString(R.string.contact_question, 3);
        TextView question = (TextView) findViewById(R.id.contactQuestionView);
        question.setText(contactQuestion);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CharSequence meckerText = "";
                boolean invalidInput = false;
                long maxMinutes;
                int minutes = 0, contacts = 0;
                try {
                    maxMinutes = (new Date()).getTime() - DataController.instance().getLastAnsweredDate().getTime();
                    maxMinutes /= 60000;
                } catch(java.lang.NullPointerException e) {
                    maxMinutes = Integer.MAX_VALUE;
                }
                try {
                    minutes = getValue(R.id.minutes) + 60*getValue(R.id.hours);
                    contacts = getValue(R.id.numberOfContacts);
                } catch(java.lang.NullPointerException e) {
                    invalidInput = true;
                    meckerText = v.getResources().getString(R.string.missingInput);
                }
                if(contacts == 0 ^ minutes == 0)
                    meckerText = v.getResources().getString(R.string.contactsMinutesMismatch);
                else if(!invalidInput && !checkValue(R.id.minutes, 59))
                    meckerText = v.getResources().getString(R.string.invalidMinutes);
                else if(minutes > maxMinutes)
                    meckerText = v.getResources().getString(R.string.tooMuchMinutes);
                else if(!invalidInput) {
                    saveValues();
                    return;
                }
                toastText(meckerText);
            }
            
            private Integer getValue(int id) {
                EditText editText = (EditText) findViewById(id);
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
                } catch(java.lang.NullPointerException e) {
                    return false;
                }
            }
            
            private void toastText(CharSequence text) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            
            private void saveValues() {
                DataController.instance().completeQuestions(
                        getValue(R.id.hours),
                        getValue(R.id.minutes),
                        getValue(R.id.numberOfContacts));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

}
