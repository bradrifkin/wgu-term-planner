package com.example.rifkinc196finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class CourseNoteEditorActivity extends AppCompatActivity {

    private String action;
    private Uri courseNoteUri;
    private Uri courseUri;
    private long courseId;
    private long courseNoteId;
    private CourseNote cn;
    private EditText noteTextField;

    public void saveCourseNote(View view) {
        if (action.equals(Intent.ACTION_INSERT)) {
            DataManager.insertCourseNote(this, courseId, noteTextField.getText().toString().trim() );
            setResult(RESULT_OK);
            finish();
        } if (action.equals(Intent.ACTION_EDIT)) {
            cn.text = noteTextField.getText().toString().trim();
            cn.saveChanges(this);
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTextField = (EditText) findViewById(R.id.etCourseNoteText);

        courseNoteUri = getIntent().getParcelableExtra(DataProvider.COURSE_NOTE_CONTENT_TYPE);
        if (courseNoteUri == null) {
            // new Note
            setTitle("Enter New Note");
            courseUri = getIntent().getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
            courseId = Long.parseLong(courseUri.getLastPathSegment());
            action = Intent.ACTION_INSERT;
        } else {
            setTitle("Edit Note");
            courseNoteId = Long.parseLong(courseNoteUri.getLastPathSegment());
            cn = DataManager.getCourseNote(this, courseNoteId);
            courseId = cn.courseId;
            noteTextField.setText(cn.text);
            action = Intent.ACTION_EDIT;
        }
    }
}
