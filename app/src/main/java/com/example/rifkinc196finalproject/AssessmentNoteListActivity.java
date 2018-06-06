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

public class AssessmentNoteListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ASSESSMENT_NOTE_EDITOR_ACTIVITY_CODE = 11111;
    private static final int ASSESSMENT_NOTE_VIEWER_ACTIVITY_CODE = 22222;

    private CursorAdapter ca;
    private Uri assessmentUri;
    private long assessmentId;

    private void bindAssessmentNoteList() {
        String[] from = { DBOpenHelper.ASSESSMENT_NOTE_TEXT };
        int[] to = { R.id.tvNoteText };

        ca = new SimpleCursorAdapter(this, R.layout.course_note_list_item, null, from, to, 0);
        DataProvider db = new DataProvider();

        ListView list = (ListView) findViewById(R.id.assessmentNoteListView);
        list.setAdapter(ca);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessmentNoteListActivity.this, AssessmentNoteViewerActivity.class);
                Uri uri = Uri.parse(DataProvider.ASSESSMENT_NOTE_URI + "/" + id);
                intent.putExtra(DataProvider.ASSESSMENT_NOTE_CONTENT_TYPE, uri);
                startActivityForResult(intent, ASSESSMENT_NOTE_VIEWER_ACTIVITY_CODE);
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataProvider.ASSESSMENT_NOTE_URI, DBOpenHelper.ASSESSMENT_NOTE_COLUMNS, DBOpenHelper.ASSESSMENT_NOTE_ASSESSMENT_ID + " = " + this.assessmentId, null, null);
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
        setContentView(R.layout.activity_assessment_note_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assessmentUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_CONTENT_TYPE);
        assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());

        bindAssessmentNoteList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentNoteListActivity.this, AssessmentNoteEditorActivity.class);
                intent.putExtra(DataProvider.ASSESSMENT_CONTENT_TYPE, assessmentUri);
                startActivityForResult(intent, ASSESSMENT_NOTE_EDITOR_ACTIVITY_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

}

