package com.example.rifkinc196finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Term {
    public String termName;
    public String termStartDate;
    public String termEndDate;
    public long termId;
    public int active;

    public void activate(Context context) {
        this.active = 1;
        saveChanges(context);
    }

    public void deactivate(Context context) {
        this.active = 0;
        saveChanges(context);
    }

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_NAME, termName);
        values.put(DBOpenHelper.TERM_START, termStartDate);
        values.put(DBOpenHelper.TERM_END, termEndDate);
        context.getContentResolver().update(DataProvider.TERM_URI, values, DBOpenHelper.TERM_TABLE_ID + "=" + termId, null);
    }

    public long getClassCount(Context context) {
        Cursor cursor = context.getContentResolver().query(DataProvider.COURSE_URI, DBOpenHelper.COURSE_COLUMNS, DBOpenHelper.COURSE_TERM_ID+ "=" + this.termId, null, null );
        int numRows = cursor.getCount();
        return numRows;
    }
}
