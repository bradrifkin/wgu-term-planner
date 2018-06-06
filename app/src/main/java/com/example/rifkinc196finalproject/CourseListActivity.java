package com.example.rifkinc196finalproject;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class CourseListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private MySimpleCursorAdapter ca;
    private long termId;
    private Uri termUri;
    private Term term;
    private static final int COURSE_VIEWER_ACTIVITY_CODE = 11111;
    private static final int COURSE_EDITOR_ACTIVITY_CODE = 22222;

    private void bindClassList() {
        String[] from = { DBOpenHelper.COURSE_NAME, DBOpenHelper.COURSE_START, DBOpenHelper.COURSE_END, DBOpenHelper.COURSE_STATUS };
        int[] to = { R.id.tvClassName, R.id.tvClassStartDate, R.id.tvClassEndDate, R.id.tvStatus };

        ca = new MySimpleCursorAdapter(this, R.layout.class_list_item, null, from, to);
        DataProvider db = new DataProvider();

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(ca);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseListActivity.this, CourseViewerActivity.class);
                Uri uri = Uri.parse(DataProvider.COURSE_URI + "/" + id);
                intent.putExtra(DataProvider.COURSE_CONTENT_TYPE, uri);
                startActivityForResult(intent, COURSE_VIEWER_ACTIVITY_CODE);
            }
        });
    }

    private void loadTermData() {
        if (termUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            termId = Long.parseLong(termUri.getLastPathSegment());
            term = DataManager.getTerm(this, termId);
            setTitle("Courses");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataProvider.COURSE_URI, DBOpenHelper.COURSE_COLUMNS, DBOpenHelper.COURSE_TERM_ID + " = " + this.termId, null, null);
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
        loadTermData();
        restartLoader();
    }


    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    public class MySimpleCursorAdapter extends android.support.v4.widget.SimpleCursorAdapter {

        public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to, 0);
        }

        @Override
        public void setViewText(TextView v, String text) {

            if (v.getId() == R.id.tvStatus) {
                String status = "";

                switch(text) {
                    case "DROPPED":
                        status = "Dropped";
                        break;
                    case "IN_PROGRESS":
                        status = "In Progress";
                        break;
                    case "PLAN_TO_TAKE":
                        status = "Plan to take";
                        break;
                    case "COMPLETED":
                        status = "Completed";
                        break;
                }

                v.setText("Status: " + status);
            } else {
                v.setText(text);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseListActivity.this, CourseEditorActivity.class);
                intent.putExtra(DataProvider.TERM_CONTENT_TYPE, termUri);
                startActivityForResult(intent, COURSE_EDITOR_ACTIVITY_CODE);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);
        loadTermData();
        bindClassList();

        getLoaderManager().initLoader(0, null, this);
    }
}
