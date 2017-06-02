package com.sandeshnimhan.outtinterview;

import android.database.sqlite.SQLiteDatabase;
import com.sandeshnimhan.outtinterview.FeedReaderContract.FeedEntry;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by nimha on 6/1/2017.
 */

public class MyDB {
    private FeedReaderDbHelper dbHelper;

    private SQLiteDatabase database;

    FeedEntry dbSchema = new FeedEntry();
    /*public final static String EMP_TABLE="MyEmployees"; // name of table

    public final static String EMP_ID="_id"; // id value for employee
    public final static String EMP_NAME="name";  // name of employee
*/
    /**
     *
     * @param context
     */
    public MyDB(Context context){
        dbHelper = new FeedReaderDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    public long createRecords(String userId, String id, String title, String body){
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_USERID, userId);
        values.put(FeedEntry.COLUMN_NAME_ID, id);
        values.put(FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedEntry.COLUMN_NAME_BODY, body);
        return database.insert(FeedEntry.TABLE_NAME, null, values);
    }

    public Cursor selectRecords() {
        String[] cols = new String[] {FeedEntry.COLUMN_NAME_USERID, FeedEntry.COLUMN_NAME_ID, FeedEntry.COLUMN_NAME_TITLE, FeedEntry.COLUMN_NAME_BODY};
        Cursor mCursor = database.query(true, FeedEntry.TABLE_NAME,cols,null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }
}
