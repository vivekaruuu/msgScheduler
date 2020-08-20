package com.example.messagescheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Intent.FLAG_INCLUDE_STOPPED_PACKAGES;

public class MainActivity extends AppCompatActivity {

    EditText inputBox;
    EditText msgBox;
    String msg;
    String number;
    private static final String TAG = "MainActivity";
    Button dateButton;
    Button timeButton;
    TextView alarmDisplay;
    static Calendar mCalendar=Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputBox = findViewById(R.id.inputBox);
        msgBox = findViewById(R.id.msgBox);
        Button button = findViewById(R.id.button);
        alarmDisplay=findViewById(R.id.alarmDisplay);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg = msgBox.getText().toString();
                number = inputBox.getText().toString();
                if(!number.isEmpty()&&!msg.isEmpty()) {
                    String dateText = DateFormat.format("EEEE, MMM d , h:mm a", mCalendar).toString();
                    alarmDisplay.setText(dateText);
                    scheduleSms(mCalendar.getTimeInMillis());
                }else{
                    Toast.makeText(getApplicationContext(),"enter all the info",Toast.LENGTH_SHORT).show();
                }


            }
        });
         dateButton=findViewById(R.id.datePick);
         timeButton=findViewById(R.id.timePick);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

    }

    public void scheduleSms(long timeInMillis){

        AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        msg = msgBox.getText().toString();
        number = inputBox.getText().toString();
        Intent intent=new Intent(this, receiver.class);
        intent.putExtra("number", number);
        intent.putExtra("msg", msg);

      //  intent.addFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1,intent,0);
        Log.d(TAG, "scheduleSms:"+ intent.getStringExtra("msg"));
        alarmManager.setExact(AlarmManager.RTC,timeInMillis,pendingIntent);

    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d", mCalendar).toString();

                dateButton.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();




    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                mCalendar.set(Calendar.HOUR, hour);
                mCalendar.set(Calendar.MINUTE, minute);
                mCalendar.set(Calendar.SECOND,0);
                String dateText = DateFormat.format("h:mm a", mCalendar).toString();
                timeButton.setText(dateText);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }


}
    //    if (
//                ContextCompat.checkSelfPermission(this,
//                Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.SEND_SMS)) {
//            }
//            else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.SEND_SMS},
//                        MY_PERMISSIONS_REQUEST_SEND_SMS);
//            }




