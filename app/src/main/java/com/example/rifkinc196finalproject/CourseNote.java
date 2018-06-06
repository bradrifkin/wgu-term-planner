package com.example.rifkinc196finalproject;

import android.content.ContentValues;
import android.content.Context;

public class CourseNote {
    public String text;
    private long courseNoteId;
    public long courseId;

    public CourseNote(long courseNoteId) {
        this.courseNoteId = courseNoteId;
    }

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTE_TEXT, text);
        values.put(DBOpenHelper.COURSE_NOTE_COURSE_ID, courseId);

        context.getContentResolver().update(DataProvider.COURSE_NOTE_URI, values, DBOpenHelper.COURSE_NOTE_TABLE_ID + "=" + courseNoteId, null);
    }
}
