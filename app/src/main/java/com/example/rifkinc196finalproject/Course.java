package com.example.rifkinc196finalproject;

import android.content.ContentValues;
import android.content.Context;

public class Course {
    public long courseId;
    public String courseName;
    public String courseStart;
    public String courseEnd;
    public String courseMentor;
    public String courseMentorEmail;
    public String courseMentorPhone;
    public boolean courseNotifications;
    public CourseStatus status;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NAME, courseName);
        values.put(DBOpenHelper.COURSE_START, courseStart);
        values.put(DBOpenHelper.COURSE_END, courseEnd);
        values.put(DBOpenHelper.COURSE_MENTOR, courseMentor);
        values.put(DBOpenHelper.COURSE_MENTOR_EMAIL, courseMentorEmail);
        values.put(DBOpenHelper.COURSE_MENTOR_PHONE, courseMentorPhone);
        values.put(DBOpenHelper.COURSE_NOTIFICATIONS, (courseNotifications) ? 1 : 0);
        values.put(DBOpenHelper.COURSE_STATUS, status.toString());

        context.getContentResolver().update(DataProvider.COURSE_URI, values, DBOpenHelper.COURSE_TABLE_ID + "=" + courseId, null);
    }
}

