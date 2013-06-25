package de.atp.requester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button button = (Button) findViewById(R.id.button1);
        EditText editText = (EditText) findViewById(R.id.editTextCode);
        if (editText.length() == 5)
            button.isClickable();

        button.setOnClickListener((OnClickListener) this);
    }
    
    private boolean codeIsValid(String c) {
        return c.matches(".*\\d.*") ? false : true;           
    }
    

    public void onClick(View v) {
        String code = ((EditText) findViewById(R.id.editTextCode)).getText().toString();
        if (codeIsValid(code))
            startActivity(new Intent(this, TimetableActivity.class));
        //TODO: abspeichern
    }

}
