package iot.project.caretaker.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Akrit on 22-Feb-18.
 */

public class DbContract {
    public DbContract() {}

    public static final String CONTENT_AUTHORITY = "iot.project.caretaker";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_REMINDERS = "reminders";

    public static final class ReminderData implements BaseColumns{
        public  static final String _ID = BaseColumns._ID ;

        public static final String RTABLE_NAME = "reminders";

        public static final String COL_TITLE = "title";

        public static final String COL_HOUR = "hour";

        public static final String COL_MINUTE = "minute";

        public static final String COL_UID = "uid";

        public static final String COL_PROCESS = "process";

        public static final String COL_REPEAT = "repeat_status";

        public static final String COL_ASTATUS = "alarm_status";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REMINDERS);

        //FOR USER TABLE
        public static final String UTABLE_NAME = "user";

        public static final String COL_UNAME = "user_name";

        public static final String COL_AGE = "user_age";

        public static final String COL_GENDER = "user_gender";

        public static final String COL_MEDICINES = "user_medicines";

        //FOR SERVER TABLE
        public static final String STABLE_NAME = "server";

        public static final String COL_SNAME = "server_name";
    }
}
