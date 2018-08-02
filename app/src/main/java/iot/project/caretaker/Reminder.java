package iot.project.caretaker;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import iot.project.caretaker.database.DbContract;

/**
 * Created by Akrit on 22-Feb-18.
 */

public class Reminder extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REM_LOADER = 1;
    ReminderCursorAdapter rCA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminders);

        ListView remList = (ListView) findViewById(R.id.reminderlist);
        FloatingActionButton fab_add_rem = (FloatingActionButton) findViewById(R.id.fab_add_reminder);

        fab_add_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Add_Reminder.class);
                startActivity(i);
                finish();
            }
        });

        rCA = new ReminderCursorAdapter(this,null,0);
        remList.setAdapter(rCA);
        //Kick off the Loader
        getLoaderManager().initLoader(REM_LOADER, null, this);

        remList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(Reminder.this, Add_Reminder.class);
                Uri currentRemUri = ContentUris.withAppendedId(DbContract.ReminderData.CONTENT_URI, id);
                i.setData(currentRemUri);
                startActivity(i);
            }
        });

      /*  remList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Uri currentRemUri = ContentUris.withAppendedId(DbContract.ReminderData.CONTENT_URI, id);
                getContentResolver().delete(currentRemUri, null, null);
                Toast.makeText(getApplicationContext(), "HOLA", Toast.LENGTH_LONG).show();
                return true;
            }
        }); */
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //Define projection that specify the coloumn
        String[] projection = {DbContract.ReminderData._ID, DbContract.ReminderData.COL_TITLE,
                DbContract.ReminderData.COL_HOUR, DbContract.ReminderData.COL_MINUTE,
                DbContract.ReminderData.COL_PROCESS, DbContract.ReminderData.COL_REPEAT};

        return new CursorLoader(this,
                DbContract.ReminderData.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //Update with this new cursor containing updated Reminder data
        rCA.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Callback called when data is need to be deleted
        rCA.swapCursor(null);
    }
}
