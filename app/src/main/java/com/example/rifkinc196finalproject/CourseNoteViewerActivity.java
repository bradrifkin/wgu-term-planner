package com.example.rifkinc196finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CourseNoteViewerActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;
    private Uri noteUri;
    private TextView tvNoteText;
    private long courseNoteId;
    private static final int COURSE_NOTE_EDITOR_ACTIVITY_CODE = 11111;
    private static final int CAMERA_ACTIVITY_CODE = 22222;

    private void loadNote() {
        CourseNote cn = DataManager.getCourseNote(this, courseNoteId);
        tvNoteText.setText(cn.text);
        tvNoteText.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadNote();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_course_note:
                return deleteCourseNote();
            case R.id.action_add_photo:
                return addPhoto();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean addPhoto() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("PARENT_URI", noteUri);
        startActivityForResult(intent, CAMERA_ACTIVITY_CODE);
        return true;
    }

    private boolean deleteCourseNote() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            DataManager.deleteCourseNote(CourseNoteViewerActivity.this, courseNoteId);
                            setResult(RESULT_OK);
                            finish();

                            Toast.makeText(CourseNoteViewerActivity.this,
                                    R.string.note_deleted,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_note)
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_note, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        CourseNote courseNote = DataManager.getCourseNote(this, courseNoteId);
        Course course = DataManager.getCourse(this, courseNote.courseId);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareSubject = course.courseName + ": Course Note";
        String shareBody = courseNote.text;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

        mShareActionProvider.setShareIntent(sharingIntent);
        return true;
    }

    public void handleEditNote(View view) {
        Intent intent = new Intent(this, CourseNoteEditorActivity.class);
        intent.putExtra(DataProvider.COURSE_NOTE_CONTENT_TYPE, noteUri);
        startActivityForResult(intent, COURSE_NOTE_EDITOR_ACTIVITY_CODE);
    }

    public void handleViewImages(View view) {
        Intent intent = new Intent(this, ImageListActivity.class);
        intent.putExtra("ParentUri", noteUri);
        startActivityForResult(intent, 0);
    }

    public void handleAddPhoto(View view) {
        addPhoto();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNoteText = (TextView) findViewById(R.id.tvNoteText);

        noteUri = getIntent().getParcelableExtra(DataProvider.COURSE_NOTE_CONTENT_TYPE);
        if (noteUri != null) {
            courseNoteId = Long.parseLong(noteUri.getLastPathSegment());
            setTitle("View Course Note");
            loadNote();
        }
    }
}
