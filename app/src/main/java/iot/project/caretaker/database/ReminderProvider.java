package iot.project.caretaker.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Akrit on 22-Feb-18.
 */

public class ReminderProvider extends ContentProvider {
    private static final int REM = 100;
    private static final int REM_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DbContract.CONTENT_AUTHORITY, DbContract.PATH_REMINDERS, REM);
        sUriMatcher.addURI(DbContract.CONTENT_AUTHORITY, DbContract.PATH_REMINDERS + "/#", REM_ID);
    }
    /** Tag for the log messages */
    public static final String LOG_TAG = ReminderProvider.class.getSimpleName();
    DbHelper mDB;
    @Override
    public boolean onCreate() {
        mDB = new DbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        //Get Readable Data
        SQLiteDatabase database = mDB.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match){
            case REM:
                cursor = database.query(DbContract.ReminderData.RTABLE_NAME,projection,selection,selectionArgs,
                        null, null,null);
                break;
            case REM_ID:
                selection = DbContract.ReminderData._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(DbContract.ReminderData.RTABLE_NAME,projection,selection,selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case REM:
                return insertRem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertRem(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDB.getWritableDatabase();

        long id = database.insert(DbContract.ReminderData.RTABLE_NAME,null,values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case REM:
                return updateRem(uri, contentValues, selection, selectionArgs);

            case REM_ID:
                selection = DbContract.ReminderData._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateRem(uri, contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateRem(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if (contentValues.containsKey(DbContract.ReminderData.COL_TITLE)){
            String name = contentValues.getAsString(DbContract.ReminderData.COL_TITLE);
            if (name == null) {
                throw new IllegalArgumentException("Title Required");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (contentValues.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDB.getWritableDatabase();

        int rowsUpdated = database.update(DbContract.ReminderData.RTABLE_NAME, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }


    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Get Writeable Data
        SQLiteDatabase database = mDB.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        switch (match){
            case REM:
                return database.delete(DbContract.ReminderData.RTABLE_NAME,selection,selectionArgs);

            case REM_ID:
                selection = DbContract.ReminderData._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(DbContract.ReminderData.RTABLE_NAME,selection,selectionArgs);

            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }

    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
}

