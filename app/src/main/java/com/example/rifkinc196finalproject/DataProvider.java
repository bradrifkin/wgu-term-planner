package com.example.rifkinc196finalproject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class DataProvider extends ContentProvider {

    private static final int TERMS = 1;
    public static final int TERMS_ID = 2;
    public static final int COURSES = 3;
    public static final int COURSES_ID = 4;
    public static final int COURSE_NOTES = 5;
    public static final int COURSE_NOTES_ID = 6;
    public static final int ASSESSMENTS = 7;
    public static final int ASSESSMENTS_ID = 8;
    public static final int ASSESSMENT_NOTES = 9;
    public static final int ASSESSMENT_NOTES_ID = 10;
    public static final int IMAGES = 11;
    public static final int IMAGES_ID = 12;

    private static final String AUTHORITY = "com.example.rifkinc196finalproject.dataprovider";
    private static final String TERM_PATH = "terms";
    public static final String COURSE_PATH = "courses";
    public static final String COURSE_NOTE_PATH = "courseNotes";
    public static final String ASSESSMENT_PATH = "assessments";
    public static final String ASSESSMENT_NOTE_PATH = "assessmentNotes";
    public static final String IMAGE_PATH = "images";

    public static final String TERM_CONTENT_TYPE = "term";
    public static final String COURSE_CONTENT_TYPE = "course";
    public static final String COURSE_NOTE_CONTENT_TYPE = "courseNote";
    public static final String ASSESSMENT_CONTENT_TYPE = "assessment";
    public static final String ASSESSMENT_NOTE_CONTENT_TYPE = "assessmentNote";
    public static final String IMAGE_CONTENT_TYPE = "image";

    public static final Uri TERM_URI = Uri.parse("content://" + AUTHORITY + "/" + TERM_PATH);
    public static final Uri COURSE_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_PATH);
    public static final Uri COURSE_NOTE_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_NOTE_PATH);
    public static final Uri ASSESSMENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENT_PATH);
    public static final Uri ASSESSMENT_NOTE_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENT_NOTE_PATH);
    public static final Uri IMAGE_URI = Uri.parse("content://" + AUTHORITY + "/" + IMAGE_PATH);

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, TERM_PATH, TERMS);
        uriMatcher.addURI(AUTHORITY, TERM_PATH + "/#", TERMS_ID);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH, COURSES);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH + "/#", COURSES_ID);
        uriMatcher.addURI(AUTHORITY, COURSE_NOTE_PATH, COURSE_NOTES);
        uriMatcher.addURI(AUTHORITY, COURSE_NOTE_PATH + "/#", COURSE_NOTES_ID);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_PATH, ASSESSMENTS);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_PATH + "/#", ASSESSMENTS_ID);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_NOTE_PATH, ASSESSMENT_NOTES);
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_NOTE_PATH + "/#", ASSESSMENT_NOTES_ID);
        uriMatcher.addURI(AUTHORITY, IMAGE_PATH, IMAGES);
        uriMatcher.addURI(AUTHORITY, IMAGE_PATH + "/#", IMAGES_ID);
    }

    private SQLiteDatabase db;
    private String currentTable;

    @Nullable
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.query(DBOpenHelper.TABLE_TERMS, DBOpenHelper.TERM_COLUMNS, selection, null, null, null, DBOpenHelper.TERM_TABLE_ID + " ASC");
            case COURSES:
                return db.query(DBOpenHelper.TABLE_COURSES, DBOpenHelper.COURSE_COLUMNS, selection, null, null, null, DBOpenHelper.COURSE_TABLE_ID + " ASC");
            case COURSES_ID:
                return db.query(DBOpenHelper.TABLE_COURSES, DBOpenHelper.COURSE_COLUMNS, DBOpenHelper.COURSE_TABLE_ID + "=" + uri.getLastPathSegment(), null, null, null, DBOpenHelper.COURSE_TABLE_ID + " ASC" );
            case COURSE_NOTES:
                return db.query(DBOpenHelper.TABLE_COURSE_NOTES, DBOpenHelper.COURSE_NOTE_COLUMNS, selection, null, null, null, DBOpenHelper.COURSE_NOTE_TABLE_ID + " ASC");
            case ASSESSMENTS:
                return db.query(DBOpenHelper.TABLE_ASSESSMENTS, DBOpenHelper.ASSESSMENT_COLUMNS, selection, null, null, null, DBOpenHelper.ASSESSMENT_TABLE_ID + " ASC");
            case ASSESSMENT_NOTES:
                return db.query(DBOpenHelper.TABLE_ASSESSMENT_NOTES, DBOpenHelper.ASSESSMENT_NOTE_COLUMNS, selection, null, null, null, DBOpenHelper.ASSESSMENT_NOTE_TABLE_ID + " ASC");
            case IMAGES:
                return db.query(DBOpenHelper.TABLE_IMAGES, DBOpenHelper.IMAGE_COLUMNS, selection, null, null, null, DBOpenHelper.IMAGE_TABLE_ID + " ASC");
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        switch (uriMatcher.match(uri)) {
            case TERMS:
                id = db.insert(DBOpenHelper.TABLE_TERMS, null, values);
                Log.d("DataProvider", "Inserted _Term: " + id);
                return Uri.parse(TERM_PATH + "/" + id);
            case COURSES:
                id = db.insert(DBOpenHelper.TABLE_COURSES, null, values);
                Log.d("DataProvider", "Inserted _Term: " + id);
                return Uri.parse(COURSE_PATH + "/" + id);
            case COURSE_NOTES:
                id = db.insert(DBOpenHelper.TABLE_COURSE_NOTES, null, values);
                Log.d("DataProvider", "Inserted _CourseNote: " + id);
                return Uri.parse(COURSE_NOTE_PATH + "/" + id);
            case ASSESSMENTS:
                id = db.insert(DBOpenHelper.TABLE_ASSESSMENTS, null, values);
                Log.d("DataProvider", "Inserted _Assessment: " + id);
                return Uri.parse(ASSESSMENT_PATH + "/" + id);
            case ASSESSMENT_NOTES:
                id = db.insert(DBOpenHelper.TABLE_ASSESSMENT_NOTES, null, values);
                Log.d("DataProvider", "Inserted _AssessmentNote: " + id);
                return Uri.parse(ASSESSMENT_NOTE_PATH + "/" + id);

            case IMAGES:
                id = db.insert(DBOpenHelper.TABLE_IMAGES, null, values);
                Log.d("DataProvider", "Inserted _Image: " + id);
                return Uri.parse(IMAGE_PATH + "/" + id);

            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.delete(DBOpenHelper.TABLE_TERMS, selection, selectionArgs);
            case COURSES:
                return db.delete(DBOpenHelper.TABLE_COURSES, selection, selectionArgs);
            case COURSE_NOTES:
                return db.delete(DBOpenHelper.TABLE_COURSE_NOTES, selection, selectionArgs);
            case ASSESSMENTS:
                return db.delete(DBOpenHelper.TABLE_ASSESSMENTS, selection, selectionArgs);
            case ASSESSMENT_NOTES:
                return db.delete(DBOpenHelper.TABLE_ASSESSMENT_NOTES, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.update(DBOpenHelper.TABLE_TERMS, values, selection, selectionArgs);
            case COURSES:
                return db.update(DBOpenHelper.TABLE_COURSES, values, selection, selectionArgs);
            case COURSE_NOTES:
                return db.update(DBOpenHelper.TABLE_COURSE_NOTES, values, selection, selectionArgs);
            case ASSESSMENTS:
                return db.update(DBOpenHelper.TABLE_ASSESSMENTS, values, selection, selectionArgs);
            case ASSESSMENT_NOTES:
                return db.update(DBOpenHelper.TABLE_ASSESSMENT_NOTES, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
        db = dbOpenHelper.getWritableDatabase();
        return true;
    }
}
