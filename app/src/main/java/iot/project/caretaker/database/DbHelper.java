package iot.project.caretaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static iot.project.caretaker.database.DbContract.ReminderData.COL_AGE;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_GENDER;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_HOUR;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_MEDICINES;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_MINUTE;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_PROCESS;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_REPEAT;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_SNAME;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_TITLE;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_UID;
import static iot.project.caretaker.database.DbContract.ReminderData.COL_UNAME;
import static iot.project.caretaker.database.DbContract.ReminderData.RTABLE_NAME;
import static iot.project.caretaker.database.DbContract.ReminderData.STABLE_NAME;
import static iot.project.caretaker.database.DbContract.ReminderData.UTABLE_NAME;
import static iot.project.caretaker.database.DbContract.ReminderData._ID;

/**
 * Created by Akrit on 22-Feb-18.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reminders.db";

    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Reminder_Table=  "CREATE TABLE " + RTABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT NOT NULL," +
                COL_HOUR + " INTEGER NOT NULL,"+
                COL_MINUTE + " INTEGER NOT NULL," +
                COL_UID + " INTEGER NOT NULL," +
                COL_PROCESS + " TEXT NOT NULL,"+
                COL_REPEAT + " TEXT NOT NULL);";

        String User_Table=  "CREATE TABLE " + UTABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_UNAME + " TEXT NOT NULL," +
                COL_AGE + " INTEGER NOT NULL," +
                COL_GENDER + " TEXT NOT NULL," +
                COL_MEDICINES + " TEXT NOT NULL);";

        String Server_Table= "CREATE TABLE " + STABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_SNAME + " TEXT NOT NULL);";

        db.execSQL(User_Table);
        db.execSQL(Reminder_Table);
        db.execSQL(Server_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RTABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UTABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STABLE_NAME);
        onCreate(db);
    }
}
