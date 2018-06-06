package com.example.rifkinc196finalproject;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CourseNoteListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter ca;
    private Uri courseUri;
    private long courseId;
    private static final int COURSE_NOTE_EDITOR_ACTIVITY_CODE = 11111;
    private static final int COURSE_NOTE_VIEWER_ACTIVITY_CODE = 22222;

        private void bindCourseNoteList() {
        String[] from = { DBOpenHelper.COURSE_NOTE_TEXT };
        int[] to = { R.id.tvNoteText };

        ca = new SimpleCursorAdapter(this, R.layout.course_note_list_item, null, from, to, 0);
        DataProvider db = new DataProvider();

        ListView list = (ListView) findViewById(R.id.courseNoteListView);
        list.setAdapter(ca);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseNoteListActivity.this, CourseNoteViewerActivity.class);
                Uri uri = Uri.parse(DataProvider.COURSE_NOTE_URI + "/" + id);
                intent.putExtra(DataProvider.COURSE_NOTE_CONTENT_TYPE, uri);
                startActivityForResult(intent, COURSE_NOTE_VIEWER_ACTIVITY_CODE);
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataProvider.COURSE_NOTE_URI, DBOpenHelper.COURSE_NOTE_COLUMNS, DBOpenHelper.COURSE_NOTE_COURSE_ID + " = " + this.courseId, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ca.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ca.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseUri = getIntent().getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
        courseId = Long.parseLong(courseUri.getLastPathSegment());

        bindCourseNoteList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseNoteListActivity.this, CourseNoteEditorActivity.class);
                intent.putExtra(DataProvider.COURSE_CONTENT_TYPE, courseUri);
                startActivityForResult(intent, COURSE_NOTE_EDITOR_ACTIVITY_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }
}
