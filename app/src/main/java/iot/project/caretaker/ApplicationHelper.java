package iot.project.caretaker;

import android.app.Application;
import android.content.Context;

import iot.project.caretaker.database.DbHelper;

public class ApplicationHelper extends Application {

    private static Application instance;
    String SERVER_URL;
    DbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dbHelper = new DbHelper(this);
        //getServerURL();

    }

   /* public void getServerURL() {

        String[] projection = {DbContract.ReminderData._ID,
                DbContract.ReminderData.COL_SNAME};
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DbContract.ReminderData.STABLE_NAME, projection,
                null,
                null,
                null,
                null,
                null);
        try {
            if (c.getCount() == 1) {
                c.moveToPosition(0);
                SERVER_URL = c.getString(c.getColumnIndex(DbContract.ReminderData.COL_SNAME));
                Toast.makeText(getApplicationContext(), SERVER_URL, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "fuck", Toast.LENGTH_LONG).show();
            }
        } finally {
            c.close();
        }
    } */



    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
