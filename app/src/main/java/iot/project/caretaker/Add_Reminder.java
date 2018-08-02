package iot.project.caretaker;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import iot.project.caretaker.database.DbContract;

import static iot.project.caretaker.database.DbContract.ReminderData.COL_HOUR;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_MINUTE;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_PROCESS;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_REPEAT;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_TITLE;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_UID;
import static iot.project.caretaker.database.DbContract.ReminderData._ID;

/**
 * Created by Akrit on 23-Feb-18.
 */

public class Add_Reminder extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final int EXISTING_REM_LOADER = 0;

    Calendar c = Calendar.getInstance();
    int initial_hour = c.get(Calendar.HOUR_OF_DAY);
    int initial_minute = c.get(Calendar.MINUTE);

    Uri mCurrentUri;
    Button delete_btn,save_btn;
    RadioGroup taskSelector;
    Switch repeatStatus, alarmStatus;
    EditText enter_title;
    TextView reminder_operation,select_time,repeatStatustv,alarmStatustv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        Intent i = getIntent(); //Check which Intent launched the activity
        mCurrentUri = i.getData(); //Get the data of Uri selected for a specific Reminder

        save_btn = (Button) findViewById(R.id.save_btn);
        delete_btn = (Button) findViewById(R.id.delete_btn);
        taskSelector = (RadioGroup) findViewById(R.id.process_selector);
        reminder_operation = (TextView) findViewById(R.id.reminder_operation);
        select_time = (TextView) findViewById(R.id.select_time);
        repeatStatustv = (TextView) findViewById(R.id.repeat_status_textView);
        alarmStatustv = (TextView) findViewById(R.id.alarm_status_textView);
        enter_title = (EditText) findViewById(R.id.enter_title);
        repeatStatus = (Switch) findViewById(R.id.repeat_switch);
        alarmStatus = (Switch) findViewById(R.id.astatus_switch);

        //Checking whether to add or edit reminder
        if (mCurrentUri == null){
            reminder_operation.setText(R.string.add_reminder);
            delete_btn.setEnabled(false); //Disable Delete button for add reminder
        }
        else {
            reminder_operation.setText(R.string.edit_reminder);
            alarmStatus.setEnabled(true);
            // Initialize a loader to read the data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_REM_LOADER, null, this);
        }
        select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(Add_Reminder.this,SelectTime,initial_hour,initial_minute,true).show();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();
                Intent i = new Intent(Add_Reminder.this, Reminder.class);
                startActivity(i);
                finish();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Add_Reminder.this);
                alertDialog.setTitle("Delete Reminder");
                alertDialog.setMessage("Are you sure ?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancleAlarrm();
                        deleteReminder();

                        Intent i = new Intent(Add_Reminder.this,Reminder.class);
                        startActivity(i);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.create();
                alertDialog.show();
            }
        });
        //SET TEXT FOR REPEAT STATUS
        repeatStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeatStatustv.setText(R.string.repeat_on);
                }
                else {
                    repeatStatustv.setText(R.string.repeat_off);
                }
            }
        });
        //SET TEXT FOR ALARM STATUS
        alarmStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    alarmStatustv.setText(R.string.active);
                }
                else {
                    alarmStatustv.setText(R.string.not_active);
                }
            }
        });

    }

    private void deleteReminder() {

        if (mCurrentUri != null){
            // Call the ContentResolver to delete the reminder at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentRemUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentUri,null,null);

            if ( rowsDeleted == 0){
                Toast.makeText(getApplicationContext(), R.string.failed_operation,Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), R.string.delete_operation,Toast.LENGTH_SHORT).show();
            }
            finish(); // Close the activity
        }
    }
    private void saveReminder() {
        String title = enter_title.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE,title);
        values.put(COL_HOUR,hour);
        values.put(COL_MINUTE,min);

        int processSelected = taskSelector.getCheckedRadioButtonId();
        boolean repeatOrnot = repeatStatus.isChecked();
        //boolean activeOrnot = alarmStatus.isChecked();

        //Get the repeat status for database
        if (repeatOrnot){
           String REPEAT_ON = "Repeat:On";
            values.put(COL_REPEAT,REPEAT_ON);
        }
        else {
          String REPEAT_OFF = "Repeat:Off";
            values.put(COL_REPEAT,REPEAT_OFF);
        }

       /* //Get the alarm status for database
        if (activeOrnot){
            String ACTIVE = "Active";
            values.put(COL_ASTATUS,ACTIVE);
        }
        else {
            String NOT_ACTIVE = "Not Active";
            values.put(COL_ASTATUS,NOT_ACTIVE);
        } */

        //Get the process into database
        if (processSelected == R.id.coffee_prc){
            String COFFEE = "Coffee";
            values.put(COL_PROCESS,COFFEE);
        }
        else if (processSelected == R.id.drink_prc){
            String DRINKS = "Drink";
            values.put(COL_PROCESS,DRINKS);
        }
        else if (processSelected == R.id.medicine_prc){
            String MEDICINE = "Medicine";
            values.put(COL_PROCESS,MEDICINE);
        }

        //which operation to be performed INSERT or UPDATE
        if (mCurrentUri == null){
            //Generating uniqueID...
            int uniqueID = (int) System.currentTimeMillis();
            //INSERTING uniqueID into DATABASE(ONLY WHILE INSERTING OPERATION)
            values.put(COL_UID,uniqueID);

            setAlarm(uniqueID,TimeInMillis);

            //INSERT data into database
            Uri newUri = getContentResolver().insert(DbContract.ReminderData.CONTENT_URI,values);

            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, R.string.failed_operation,
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this,R.string.insert_operation,
                        Toast.LENGTH_SHORT).show();
            }
        }

        else {
            //Enabling alarm status button
            alarmStatus.setEnabled(true);
            //UPDATING COL_UID with its OWN mUid to prevent creating multiple Alarm....
            values.put(COL_UID,mUid);
            //Getting mUid for Cursor loader
                setAlarm(mUid, TimeInMillis);
            //UPDATE THE DATA

            int rowsaffected = getContentResolver().update(mCurrentUri, //mcurrentRemUri because we are triggring a specific reminder
                    values,
                    null,
                    null);

            if (rowsaffected == 0){
                Toast.makeText(this, R.string.failed_operation,
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,R.string.update_operation,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setAlarm(int id, long milli) {

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int processSelected = taskSelector.getCheckedRadioButtonId();
        boolean repeatOrnot = repeatStatus.isChecked(); // true

        if (processSelected == R.id.coffee_prc){
            Intent intent = new Intent(Add_Reminder.this, CoffeeProcessReciever.class);
            PendingIntent pi_coffee = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (repeatOrnot){
                am.setRepeating(AlarmManager.RTC_WAKEUP,milli,24*60*60*1000,pi_coffee);
            }
            else {
                am.set(AlarmManager.RTC_WAKEUP,milli,pi_coffee);
            }
        }
        else if (processSelected == R.id.drink_prc){
            Intent intent = new Intent(Add_Reminder.this, DrinkProcessReciever.class);
            PendingIntent pi_drink = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (repeatOrnot){
                am.setRepeating(AlarmManager.RTC_WAKEUP,milli,24*60*60*1000,pi_drink);
            }
            else {
                am.set(AlarmManager.RTC_WAKEUP,milli,pi_drink);
            }
        }
        else if (processSelected == R.id.medicine_prc){
            Intent intent = new Intent(Add_Reminder.this, MedicineProcessReciever.class);
            PendingIntent pi_medi = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (repeatOrnot){
                am.setRepeating(AlarmManager.RTC_WAKEUP,milli,24*60*60*1000,pi_medi);
            }
            else {
                am.set(AlarmManager.RTC_WAKEUP,milli,pi_medi);
            }
        }

    }

    private void cancleAlarrm(){
       // int id = 1; //Temporary REMOVE THIS
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        int processSelected = taskSelector.getCheckedRadioButtonId();
        if (processSelected == R.id.coffee_prc){
            Intent intent = new Intent(Add_Reminder.this, CoffeeProcessReciever.class);
            PendingIntent pi_coffee = PendingIntent.getBroadcast(getApplicationContext(), mUid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pi_coffee);
        }
        else if (processSelected == R.id.drink_prc){
            Intent intent = new Intent(Add_Reminder.this, DrinkProcessReciever.class);
            PendingIntent pi_drink = PendingIntent.getBroadcast(getApplicationContext(), mUid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pi_drink);
        }

        else if (processSelected == R.id.medicine_prc){
            Intent intent = new Intent(Add_Reminder.this, MedicineProcessReciever.class);
            PendingIntent pi_medi = PendingIntent.getBroadcast(getApplicationContext(), mUid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pi_medi);
        }
    }

    long TimeInMillis;
    int hour,min;
    TimePickerDialog.OnTimeSetListener SelectTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            c.set(Calendar.HOUR_OF_DAY,hourOfDay);
            c.set(Calendar.MINUTE,minute);
            hour = hourOfDay;
            min = minute;
            TimeInMillis = c.getTimeInMillis();
            select_time.setText(String.format("%02d:%02d",hourOfDay,minute));
        }
    };
    int mUid;
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {_ID,COL_TITLE,COL_HOUR,COL_MINUTE,COL_UID,COL_PROCESS,COL_REPEAT};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                mCurrentUri, //mcurrentRemUri because we are triggering a specific reminder
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            // Extract out the value from the Cursor for the given column index
            int titleIndex = cursor.getColumnIndex(DbContract.ReminderData.COL_TITLE);
            int hourIndex = cursor.getColumnIndex(DbContract.ReminderData.COL_HOUR);
            int minuteIndex = cursor.getColumnIndex(DbContract.ReminderData.COL_MINUTE);
            int uidIndex = cursor.getColumnIndex(DbContract.ReminderData.COL_UID);
            //int processIndex = cursor.getColumnIndex(DbContract.ReminderData.COL_PROCESS);
            //int repeat_statusIndex = cursor.getColumnIndex(DbContract.ReminderData.COL_REPEAT);

            String mtitle = cursor.getString(titleIndex);
            //String mprocess = cursor.getString(processIndex);
            //String mRepeat = cursor.getString(repeat_statusIndex);
            int mhour = cursor.getInt(hourIndex);
            int mminute = cursor.getInt(minuteIndex);
            mUid = cursor.getInt(uidIndex);

            enter_title.setText(mtitle);
            select_time.setText(String.format("%02d:%02d",mhour,mminute));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
