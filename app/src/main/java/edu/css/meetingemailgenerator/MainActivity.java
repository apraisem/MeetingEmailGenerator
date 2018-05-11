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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    DatabaseReference myDbRef;


    //onCreate method, initializes variables and calls methods to spinners and buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up widget links to layout and call methods to spinners and buttons
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDate = (EditText) findViewById(R.id.etDate);
        selectStartTime();
        selectHowLong();
        selectRepeated();
        etMtgDesc = (EditText) findViewById(R.id.etMtgDesc);
        addListenerOnButtonEmail();
        addListenerOnButtonText();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myDbRef = database.getReference("My Meetings");
    }


    //Called from the onCreate method. Spinner method for setting meeting start time
    private void selectStartTime() {
        //set up start time to layout
        spStartTime = (Spinner) findViewById(R.id.spStartTime);
        //get data from the array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.start_time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set to adapter
        spStartTime.setAdapter(adapter);

    }
    //Called from the onCreate method. Spinner method for setting meeting length
    private void selectHowLong(){
        //set up length to layout
        spLength = (Spinner) findViewById(R.id.spLength);
        //get data from the array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.meeting_length_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set to adapter
        spLength.setAdapter(adapter);

    }
    //Called from the onCreate method. Spinner method for selecting repeat method
    private void selectRepeated(){
        //set up repeat to layout
        spRepeated = (Spinner) findViewById(R.id.spRepeated);
        //get data from the array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.repeated_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set to adapter
        spRepeated.setAdapter(adapter);


    }
    //Called from the onCreate method. When Send Emai button is selected,
    //then the conversion method is called and the data is pulled into the email subject and message.

    private void addListenerOnButtonEmail() {
    btnEmail = (Button) findViewById(R.id.btnEmail);

    btnEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //call method to convert layout data to strings
            conversion();
            Log.d("CIS3334", "Saving Meeting: ");        // debugging log
            // ---- Get a new database key for the meeting
            String key = myDbRef.child("All My Meetings").push().getKey();
            //set data to value in database
            myDbRef.child("All My Meetings").child(key).setValue("Meeting Title: " + mtgTitle +
                    '\n' + "Date: " + mtgDate +
                    '\n' + "Start Time: " + mtgStartTime +
                    '\n' + "Length of Meeting (in hours): " + mtgLength +
                    '\n' + "Repeated?: " + mtgRepeated +
                    '\n' + "Meeting Description: " + mtgMtgDesc);

            //start email intent
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            //set email subject
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Meeting: " + mtgTitle);
            //Set email body
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Meeting Title: " + mtgTitle +
                    '\n' + "Date: " + mtgDate +
                    '\n' + "Start Time: " + mtgStartTime +
                    '\n' + "Length of Meeting (in hours): " + mtgLength +
                    '\n' + "Repeated?: " + mtgRepeated +
                    '\n' + "Meeting Description: " + mtgMtgDesc);
            //set email intent type
            emailIntent.setType("vnd.android.cursor.dir/event");
            //start activity
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
                //call method to convert layout data to strings
                conversion();
                Log.d("CIS3334", "Saving Meeting: ");        // debugging log
                // ---- Get a new database key for the meeting
                String key = myDbRef.child("All My Meetings").push().getKey();
                //set data to value in database
                myDbRef.child("All My Meetings").child(key).setValue("Meeting Title: " + mtgTitle +
                        '\n' + "Date: " + mtgDate +
                        '\n' + "Start Time: " + mtgStartTime +
                        '\n' + "Length of Meeting (in hours): " + mtgLength +
                        '\n' + "Repeated?: " + mtgRepeated +
                        '\n' + "Meeting Description: " + mtgMtgDesc);

                Log.i("Send SMS", ""); //debugging log
                //start sms intent
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                //set sms body
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
        //converts data entered in layout to strings
        mtgTitle = etTitle.getText().toString();
        mtgDate = etDate.getText().toString();
        //converts data selected in spinners to strings
        mtgStartTime = (String) spStartTime.getSelectedItem().toString();
        mtgLength = (String) spLength.getSelectedItem().toString();
        mtgRepeated = (String) spRepeated.getSelectedItem().toString();
        //converts data entered in layout to string
        mtgMtgDesc = etMtgDesc.getText().toString();
    }
}
