package com.example.rifkinc196finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wgu_classes.db";
    private static final int DATABASE_VERSION = 11;

    public static final String TABLE_COURSES = "courses";
    public static final String COURSE_TERM_ID = "termId";
    public static final String COURSE_TABLE_ID = "_id";
    public static final String COURSE_NAME = "name";
    public static final String COURSE_START = "start";
    public static final String COURSE_END = "end";
    public static final String COURSE_CREATED = "_created";
    public static final String COURSE_DESCRIPTION = "description";
    public static final String COURSE_MENTOR = "mentor";
    public static final String COURSE_MENTOR_PHONE = "mentorPhone";
    public static final String COURSE_MENTOR_EMAIL = "mentorEmail";
    public static final String COURSE_STATUS = "status";
    public static final String COURSE_NOTIFICATIONS = "notifications";

    public static final String TABLE_TERMS = "terms";
    public static final String TERM_TABLE_ID = "_id";
    public static final String TERM_NAME = "name";
    public static final String TERM_START = "start";
    public static final String TERM_END = "end";
    public static final String TERM_ACTIVE = "active";
    public static final String TERM_CREATED = "_created";

    public static final String TABLE_COURSE_NOTES = "course_notes";
    public static final String COURSE_NOTE_TABLE_ID = "_id";
    public static final String COURSE_NOTE_COURSE_ID = "courseId";
    public static final String COURSE_NOTE_TEXT = "text";
    public static final String COURSE_NOTE_ATTACHMENT_URI = "uri";
    public static final String COURSE_NOTE_CREATED = "_created";

    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String ASSESSMENT_TABLE_ID = "_id";
    public static final String ASSESSMENT_COURSE_ID = "courseId";
    public static final String ASSESSMENT_CODE = "code";
    public static final String ASSESSMENT_NAME = "name";
    public static final String ASSESSMENT_DATE_TIME = "dateTime";
    public static final String ASSESSMENT_DESCRIPTION = "description";
    public static final String ASSESSMENT_CREATED = "_created";
    public static final String ASSESSMENT_NOTIFICATIONS = "notifications";

    public static final String TABLE_ASSESSMENT_NOTES = "assessment_notes";
    public static final String ASSESSMENT_NOTE_TABLE_ID = "_id";
    public static final String ASSESSMENT_NOTE_ASSESSMENT_ID = "assessmentId";
    public static final String ASSESSMENT_NOTE_TEXT = "text";
    public static final String ASSESSMENT_NOTE_ATTACHMENT_URI = "uri";
    public static final String ASSESSMENT_NOTE_CREATED = "_created";

    public static final String TABLE_IMAGES = "images";
    public static final String IMAGE_TABLE_ID = "_id";
    public static final String IMAGE_PARENT_URI = "uri";
    public static final String IMAGE_TIMESTAMP = "timestamp";
    public static final String IMAGE_CREATED = "_created";

    public static final String[] COURSE_COLUMNS = {COURSE_TABLE_ID, COURSE_NAME, COURSE_START,
            COURSE_END, COURSE_CREATED, COURSE_DESCRIPTION, COURSE_MENTOR, COURSE_MENTOR_EMAIL,
            COURSE_MENTOR_PHONE, COURSE_NOTIFICATIONS, COURSE_STATUS};

    public static final String[] TERM_COLUMNS = {TERM_TABLE_ID, TERM_NAME, TERM_START,
            TERM_END, TERM_ACTIVE, TERM_CREATED};

    public static final String[] COURSE_NOTE_COLUMNS = {COURSE_NOTE_TABLE_ID, COURSE_NOTE_COURSE_ID,
            COURSE_NOTE_TEXT, COURSE_NOTE_ATTACHMENT_URI};

    public static final String[] ASSESSMENT_COLUMNS = {ASSESSMENT_TABLE_ID, ASSESSMENT_COURSE_ID,
            ASSESSMENT_CODE, ASSESSMENT_NAME, ASSESSMENT_DESCRIPTION, ASSESSMENT_DATE_TIME,
            ASSESSMENT_NOTIFICATIONS};

    public static final String[] ASSESSMENT_NOTE_COLUMNS = {ASSESSMENT_NOTE_TABLE_ID,
            ASSESSMENT_NOTE_ASSESSMENT_ID, ASSESSMENT_NOTE_TEXT, ASSESSMENT_NOTE_ATTACHMENT_URI};

    public static final String[] IMAGE_COLUMNS = {IMAGE_TABLE_ID, IMAGE_PARENT_URI, IMAGE_TIMESTAMP};

    public static final String IMAGE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_IMAGES + " (" +
                    IMAGE_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    IMAGE_TIMESTAMP + " INTEGER, " +
                    IMAGE_PARENT_URI + " TEXT, " +
                    IMAGE_CREATED + " TEXT default CURRENT_TIMESTAMP " +
                    ")";

    public static final String COURSE_NOTE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_COURSE_NOTES + " (" +
                    COURSE_NOTE_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_NOTE_COURSE_ID + " INTEGER, " +
                    COURSE_NOTE_TEXT + " TEXT, " +
                    COURSE_NOTE_ATTACHMENT_URI + " TEXT, " +
                    COURSE_NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COURSE_NOTE_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSE_TABLE_ID + ")" +
                    ")";

    public static final String ASSESSMENT_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ASSESSMENTS + " (" +
                    ASSESSMENT_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENT_COURSE_ID + " INTEGER, " +
                    ASSESSMENT_CODE + " TEXT, " +
                    ASSESSMENT_NAME + " TEXT, " +
                    ASSESSMENT_DESCRIPTION + " TEXT, " +
                    ASSESSMENT_DATE_TIME + " TEXT, " +
                    ASSESSMENT_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    ASSESSMENT_NOTIFICATIONS + " INTEGER, " +
                    "FOREIGN KEY(" + ASSESSMENT_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSE_TABLE_ID + ")" +
                    ")";

    public static final String ASSESSMENT_NOTE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ASSESSMENT_NOTES + " (" +
                    ASSESSMENT_NOTE_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENT_NOTE_ASSESSMENT_ID + " INTEGER, " +
                    ASSESSMENT_NOTE_TEXT + " TEXT, " +
                    ASSESSMENT_NOTE_ATTACHMENT_URI + " TEXT, " +
                    ASSESSMENT_NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + ASSESSMENT_NOTE_ASSESSMENT_ID + ") REFERENCES " + TABLE_ASSESSMENTS + "(" + ASSESSMENT_TABLE_ID + ")" +
                    ")";

    private static final String TERM_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TERMS + " (" +
                    TERM_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_NAME + " TEXT, " +
                    TERM_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    TERM_START + " DATE, " +
                    TERM_END + "DATE, " +
                    TERM_ACTIVE + " INTEGER" +
                    ")";

    private static final String COURSE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_COURSES + " (" +
                    COURSE_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_TERM_ID + " INTEGER, " +
                    COURSE_NAME + " TEXT, " +
                    COURSE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    COURSE_START + " DATE, " +
                    COURSE_END + "DATE, " +
                    COURSE_DESCRIPTION + " TEXT, " +
                    COURSE_MENTOR + " TEXT, " +
                    COURSE_MENTOR_EMAIL + " TEXT, " +
                    COURSE_MENTOR_PHONE + " TEXT, " +
                    COURSE_STATUS + " TEXT, " +
                    COURSE_NOTIFICATIONS + " INTEGER, " +
                    "FOREIGN KEY(" + COURSE_TERM_ID + ") REFERENCES " + TABLE_TERMS + "(" + TERM_TABLE_ID + ")" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TERM_TABLE_CREATE);
        db.execSQL(COURSE_TABLE_CREATE);
        db.execSQL(COURSE_NOTE_TABLE_CREATE);
        db.execSQL(ASSESSMENT_TABLE_CREATE);
        db.execSQL(ASSESSMENT_NOTE_TABLE_CREATE);
        db.execSQL(IMAGE_TABLE_CREATE);
    }
}
