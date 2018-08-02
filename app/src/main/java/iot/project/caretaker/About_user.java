package iot.project.caretaker;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import iot.project.caretaker.database.DbContract;
import iot.project.caretaker.database.DbHelper;

/**
 * Created by Akrit on 28-Feb-18.
 */

public class About_user extends AppCompatActivity{

    Button edit_user;
    TextView uname,uage,ugender,umedicines,delete_record;
    DbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_user);

        edit_user = (Button) findViewById(R.id.uedit_btn);
        uname = (TextView) findViewById(R.id.uname);
        uage = (TextView) findViewById(R.id.uage);
        ugender = (TextView) findViewById(R.id.ugender);
        umedicines = (TextView) findViewById(R.id.umedicine);
        delete_record = (TextView) findViewById(R.id.delete_urecord);

        dbHelper = new DbHelper(this);

        db = dbHelper.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, DbContract.ReminderData.UTABLE_NAME);

        if (count == 0){
            edit_user.setText("Add user information ");
            delete_record.setVisibility(View.INVISIBLE);

            edit_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),Add_User.class);
                    startActivity(i);
                    finish();
                }
            });
        }
        else{
            edit_user.setText("Edit Medicines");
            delete_record.setVisibility(View.VISIBLE);

            edit_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(About_user.this);
                    alertDialog.setTitle("Update Medicine");
                    alertDialog.setMessage("Update Your Medicines");
                    alertDialog.setCancelable(false);
                    final EditText edit_meds = new EditText(About_user.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    edit_meds.setLayoutParams(lp);
                    alertDialog.setView(edit_meds);
                    alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db = dbHelper.getWritableDatabase();
                            String newMeds = edit_meds.getText().toString();
                            ContentValues values = new ContentValues();
                            values.put(DbContract.ReminderData.COL_MEDICINES,newMeds);
                            db.update(DbContract.ReminderData.UTABLE_NAME, values, null, null);
                            recreate();
                        }
                    });
                    alertDialog.setNegativeButton("Don't Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                }
            });

            delete_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(About_user.this);
                    alertDialog.setTitle("Delete User Data");
                    alertDialog.setMessage("Are You Sure ?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db = dbHelper.getWritableDatabase();
                            db.delete(DbContract.ReminderData.UTABLE_NAME, null, null);
                            recreate();
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayInfo();
    }


    private void displayInfo() {
        db = dbHelper.getReadableDatabase();

        String [] projection = {DbContract.ReminderData._ID,
                DbContract.ReminderData.COL_UNAME,
                DbContract.ReminderData.COL_AGE,
                DbContract.ReminderData.COL_GENDER,
                DbContract.ReminderData.COL_MEDICINES};

        Cursor c = db.query(DbContract.ReminderData.UTABLE_NAME,projection,
                null,
                null ,
                null ,
                null,
                null);

        int nameColIndex = c.getColumnIndex(DbContract.ReminderData.COL_UNAME);
        int ageColIndex = c.getColumnIndex(DbContract.ReminderData.COL_AGE);
        int genderColIndex = c.getColumnIndex(DbContract.ReminderData.COL_GENDER);
        int medicinesColIndex = c.getColumnIndex(DbContract.ReminderData.COL_MEDICINES);
        try{
            while (c.moveToNext()) {
                String uName = c.getString(nameColIndex);
                int uAge = c.getInt(ageColIndex);
                String uGender = c.getString(genderColIndex);
                String uMedicines = c.getString(medicinesColIndex);

                uname.setText(uName);
                uage.setText(String.valueOf(uAge));
                ugender.setText(uGender);
                umedicines.setText(uMedicines);

            }
        }
        finally {
            c.close();
        }
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.edituser:
                return true;
        }
        return super.onOptionsItemSelected(item);
    } */
}
