package edu.css.meetingemailgenerator;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

//This activity displays all editable fields and allows an email or text message to be sent using the content
//entered in via the UI. Users will use their devices email and text contact lists to send meeting info.
public class MainActivity extends AppCompatActivity {
    //Create variables
    EditText etTitle;
        String mtgTitle;
    EditText etDate;
        String mtgDate;
    Spinner spStartTime;
        String mtgStartTime;
    Spinner spLength;
        String mtgLength;
    Spinner spRepeated;
        String mtgRepeated;
    EditText etMtgDesc;
        String mtgMtgDesc;
    Button btnEmail;
    Button btnText;


    //onCreate method, initializes variables and calls methods to spinners and buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize variables and calls methods
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDate = (EditText) findViewById(R.id.etDate);
        selectStartTime();
        selectHowLong();
        selectRepeated();
        etMtgDesc = (EditText) findViewById(R.id.etMtgDesc);
        addListenerOnButtonEmail();
        addListenerOnButtonText();
    }


    //Called from the onCreate method. Spinner method for setting start time
    private void selectStartTime() {
        spStartTime = (Spinner) findViewById(R.id.spStartTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.start_time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStartTime.setAdapter(adapter);

    }
    //Called from the onCreate method. Spinner method for setting meeting length
    private void selectHowLong(){
        spLength = (Spinner) findViewById(R.id.spLength);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.meeting_length_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLength.setAdapter(adapter);

    }
    //Called from the onCreate method. Spinner method for selecting repeat method
    private void selectRepeated(){
        spRepeated = (Spinner) findViewById(R.id.spRepeated);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.repeated_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepeated.setAdapter(adapter);


    }
    //Called from the onCreate method. When Send Emai button is selected,
    //then the conversion method is called and the data is pulled into the email subject and message.

    private void addListenerOnButtonEmail() {
    btnEmail = (Button) findViewById(R.id.btnEmail);

    btnEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            conversion();
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Meeting: " + mtgTitle);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Meeting Title: " + mtgTitle +
                    '\n' + "Date: " + mtgDate +
                    '\n' + "Start Time: " + mtgStartTime +
                    '\n' + "Length of Meeting (in hours): " + mtgLength +
                    '\n' + "Repeated?: " + mtgRepeated +
                    '\n' + "Meeting Description: " + mtgMtgDesc);

            emailIntent.setType("vnd.android.cursor.dir/event");
            startActivity(emailIntent);
            finish();
        }
    });
    }
    //Called from the onCreate method. Used to send data keyed via text message.
    private void addListenerOnButtonText() {
        btnText = (Button) findViewById(R.id.btnText);
        btnText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                conversion();
                Log.i("Send SMS", "");
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("sms_body"  , "Meeting Title: " + mtgTitle +
                                                   '\n' + "Date: " + mtgDate +
                                                   '\n' + "Start Time: " + mtgStartTime +
                                                   '\n' + "Length of Meeting (in hours): " + mtgLength +
                                                   '\n' + "Repeated?: " + mtgRepeated +
                                                   '\n' + "Meeting Description: " + mtgMtgDesc);
                try {
                    startActivity(smsIntent);
                    finish();
                    Log.i("Finished sending SMS...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this,
                            "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
                                           }
                                       }
    });
    }
//Called from the onClick method. Sets all variables to string
    private void conversion() {
        mtgTitle = etTitle.getText().toString();
        mtgDate = etDate.getText().toString();

 //  I'm unsure how to get the items selected in the spinners to display
        mtgStartTime = (String) spStartTime.getSelectedItem().toString();
        mtgLength = (String) spLength.getSelectedItem().toString();
        mtgRepeated = (String) spRepeated.getSelectedItem().toString();

        mtgMtgDesc = etMtgDesc.getText().toString();
    }
}
