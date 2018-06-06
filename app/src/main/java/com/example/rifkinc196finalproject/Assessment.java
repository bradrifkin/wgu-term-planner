package com.example.rifkinc196finalproject;

import android.content.ContentValues;
import android.content.Context;

public class Assessment {
    private long assessmentId;
    public String code;
    public String name;
    public long courseId;
    public String description;
    public String dateTime;
    public int assessmentNotifications;

    public Assessment(long id) {
        assessmentId = id;
    }
    public Assessment() {}

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_COURSE_ID, courseId);
        values.put(DBOpenHelper.ASSESSMENT_CODE, code);
        values.put(DBOpenHelper.ASSESSMENT_NAME, name);
        values.put(DBOpenHelper.ASSESSMENT_DATE_TIME, dateTime);
        values.put(DBOpenHelper.ASSESSMENT_DESCRIPTION, description);
        values.put(DBOpenHelper.ASSESSMENT_NOTIFICATIONS, assessmentNotifications);

        context.getContentResolver().update(DataProvider.ASSESSMENT_URI, values, DBOpenHelper.ASSESSMENT_TABLE_ID + "=" + assessmentId, null);
    }
}
