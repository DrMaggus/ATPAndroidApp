package de.atp.requester;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SurveyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey);
		String contactQuestion = this.getResources().getString(R.string.contact_question, 3);
		TextView question = (TextView)findViewById(R.id.contactQuestionView);
		question.setText(contactQuestion);
		Button sendButton = (Button)findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(checkValue(R.id.hours, 20) && checkValue(R.id.minutes, 59) && checkValue(R.id.numberOfContacts, Integer.MAX_VALUE)) {
					// TODO finish
					((TextView)findViewById(R.id.contactQuestionView)).setText("[nächste Seite]");
				} else {
					// TODO meckern
					((TextView)findViewById(R.id.contactQuestionView)).setText("*mecker*");
				}
			}
			
			private boolean checkValue(int id, int limit) {
				EditText editText = (EditText)findViewById(id);
				String text = editText.getText().toString();
				try {
					return (Integer.parseInt(text) <= limit);
				} catch(java.lang.NumberFormatException e) {
					return false;
				}
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
