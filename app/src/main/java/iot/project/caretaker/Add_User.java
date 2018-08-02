package iot.project.caretaker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import iot.project.caretaker.database.DbContract;
import iot.project.caretaker.database.DbHelper;

/**
 * Created by Akrit on 27-Mar-18.
 */

public class Add_User extends AppCompatActivity {

    DbHelper dbHelper;
    SQLiteDatabase db;
    Button save;
    EditText uname,uage,umedi;
    RadioGroup genderSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        save = (Button) findViewById(R.id.save_user);
        uname = (EditText) findViewById(R.id.enter_name);
        uage = (EditText) findViewById(R.id.enter_age);
        umedi = (EditText) findViewById(R.id.add_medicines);
        genderSelector = (RadioGroup) findViewById(R.id.gender_selector);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
                Intent i = new Intent(getApplicationContext(),About_user.class);
                startActivity(i);
                finish();
            }
        });

    }

   /* private void updateUser() {
        String name = uname.getText().toString().trim();
        String age = uage.getText().toString().trim();
        String medi = umedi.getText().toString().trim();

        int genderSelected = genderSelector.getCheckedRadioButtonId();
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
        //Inserting Dummy Data
        ContentValues values = new ContentValues();
        values.put(DbContract.ReminderData.COL_UNAME,name);
        values.put(DbContract.ReminderData.COL_AGE,age);
        if (genderSelected == R.id.male_gender)
        {
            values.put(DbContract.ReminderData.COL_GENDER,"Male");
        }
        else
        {
            values.put(DbContract.ReminderData.COL_GENDER,"Female");
        }
        values.put(DbContract.ReminderData.COL_MEDICINES,medi);

        String selection = DbContract.ReminderData._ID + "= 1";


        db.update(DbContract.ReminderData.UTABLE_NAME, values, selection, null);
    } */

    private void insertUser() {
        String name = uname.getText().toString().trim();
        String age = uage.getText().toString().trim();
        String medi = umedi.getText().toString().trim();

        int genderSelected = genderSelector.getCheckedRadioButtonId();
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
        //Inserting Dummy Data
        ContentValues values = new ContentValues();
        values.put(DbContract.ReminderData.COL_UNAME,name);
        values.put(DbContract.ReminderData.COL_AGE,age);
        if (genderSelected == R.id.male_gender)
        {
            values.put(DbContract.ReminderData.COL_GENDER,"Male");
        }
        else
        {
            values.put(DbContract.ReminderData.COL_GENDER,"Female");
        }
        values.put(DbContract.ReminderData.COL_MEDICINES,medi);

        long newRow = db.insert(DbContract.ReminderData.UTABLE_NAME,null,values);

        if (newRow == -1){
            Toast.makeText(getApplicationContext(),R.string.failed_operation,Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"User Saved",Toast.LENGTH_SHORT).show();
        }
    }
}