package de.atp.requester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.atp.controller.DataController;

public class StartActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!DataController.isProbandFileExisting()) {
            setContentView(R.layout.activity_start);
            Button button = (Button) findViewById(R.id.button1);
            button.setOnClickListener((OnClickListener) this);
        } else {
            startActivity(new Intent(this, InfoActivity.class));
            finish();
        }
    }

    private boolean codeIsValid(String c) {
        if (!c.matches(".*\\d.*") && (c.matches("....."))) {
            return true;
        } else {
            Toast.makeText(this, "Falsche Eingabe!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void onClick(View v) {
        String code = ((EditText) findViewById(R.id.editTextCode)).getText().toString();
        if (codeIsValid(code)) {
            DataController.instance(code);
            startActivity(new Intent(this, TimetableActivity.class));
            finish();
        }
    }

}
