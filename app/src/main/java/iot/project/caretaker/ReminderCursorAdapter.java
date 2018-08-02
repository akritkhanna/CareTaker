package iot.project.caretaker;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import iot.project.caretaker.database.DbContract;

/**
 * Created by Akrit on 23-Feb-18.
 */

public class ReminderCursorAdapter extends CursorAdapter {


    public ReminderCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_look,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_title = (TextView) view.findViewById(R.id.title_l);
        TextView tv_time = (TextView) view.findViewById(R.id.time_l);
        TextView tv_repeat = (TextView) view.findViewById(R.id.repeat_tv);
        //TextView tv_aStatus = (TextView) view.findViewById(R.id.active_status);
        TextView tv_process = (TextView) view.findViewById(R.id.process);

        String title = cursor.getString(cursor.getColumnIndex(DbContract.ReminderData.COL_TITLE));
        String process = cursor.getString(cursor.getColumnIndex(DbContract.ReminderData.COL_PROCESS));
        String repeat_status = cursor.getString(cursor.getColumnIndex(DbContract.ReminderData.COL_REPEAT));
        //String active_status = cursor.getString(cursor.getColumnIndex(DbContract.ReminderData.COL_ASTATUS));
        int hour = cursor.getInt(cursor.getColumnIndex(DbContract.ReminderData.COL_HOUR));
        int minute = cursor.getInt(cursor.getColumnIndex(DbContract.ReminderData.COL_MINUTE));

        tv_title.setText(title);
        tv_process.setText(process);
        tv_repeat.setText(repeat_status);
        //tv_aStatus.setText(active_status);
        //tv_time.setText(String.format("%02d:%02d",hour,minute));
        tv_time.setText(String.format("%02d\n" + " %02d",hour,minute));
    }
}
