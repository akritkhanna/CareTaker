package iot.project.caretaker.interfacing_bridge;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import iot.project.caretaker.ApplicationHelper;
import iot.project.caretaker.R;
import iot.project.caretaker.database.DbContract;
import iot.project.caretaker.database.DbHelper;

/**
 * Created by Akrit on 12-Mar-18.
 */

public class Server extends AppCompatActivity {
    DbHelper dbHelper;
    SQLiteDatabase db;
    Button save;
    EditText ed_server;
    private String BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_server);

        save = (Button) findViewById(R.id.save_server);
        ed_server = (EditText) findViewById(R.id.edit_server);

        dbHelper = new DbHelper(this);

        db = dbHelper.getWritableDatabase();
        final long count = DatabaseUtils.queryNumEntries(db, DbContract.ReminderData.STABLE_NAME);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0){
                    insertServer();
                }
                else {
                    updateServer();
                }
                finish();
            }
        });
    }

    public Processes getProcess() {

        dbHelper = new DbHelper(ApplicationHelper.getContext());
        db = dbHelper.getReadableDatabase();

        String [] projection = {DbContract.ReminderData._ID,
                DbContract.ReminderData.COL_SNAME};

        Cursor c = db.query(DbContract.ReminderData.STABLE_NAME, projection,
                null,
                null,
                null,
                null,
                null);

        try  {
            if (c.getCount() == 1) {

                c.moveToPosition(0);
                BASE_URL = c.getString(c.getColumnIndex(DbContract.ReminderData.COL_SNAME));

            }

        }finally {
            c.close();
        }
        //BASE_URL = applicationHelper.getServerURL();
        RetrofitClient retrofitClient = new RetrofitClient();
        return retrofitClient.getClient(BASE_URL).create(Processes.class); //Getting all the result together
    }


    private void updateServer() {
        String server_url = ed_server.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DbContract.ReminderData.COL_SNAME, server_url);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        long rowUpdated = db.update(DbContract.ReminderData.STABLE_NAME,values, null, null);

        if (rowUpdated != 0){
            Toast.makeText(getApplicationContext(),"Server Updated",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),R.string.failed_operation,Toast.LENGTH_SHORT).show();
        }
    }

    private void insertServer() {
        String server_url = ed_server.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DbContract.ReminderData.COL_SNAME, server_url);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        long newRow = db.insert(DbContract.ReminderData.STABLE_NAME,null,values);

        if (newRow == -1){
            Toast.makeText(getApplicationContext(),R.string.failed_operation,Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Server Changed",Toast.LENGTH_SHORT).show();
        }
    }
}