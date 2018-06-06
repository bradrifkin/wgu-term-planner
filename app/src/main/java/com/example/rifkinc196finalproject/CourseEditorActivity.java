package com.example.rifkinc196finalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import java.util.Calendar;

public class CourseEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private String action;
    private Course course;

    private Uri termUri;
    private Uri courseUri;

    private EditText etCourseName;
    private EditText etCourseStart;
    private EditText etCourseEnd;
    private EditText etCourseMentor;
    private EditText etCourseMentorPhone;
    private EditText etCourseMentorEmail;

    private DatePickerDialog courseStartDateDialog;
    private DatePickerDialog courseEndDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViews();

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
        termUri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);

        // no uri means we're creating a new term
        if (courseUri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.add_new_course));
        } else {
            action = Intent.ACTION_EDIT;
            setTitle("Edit Course");
            long classId = Long.parseLong(courseUri.getLastPathSegment());
            course = DataManager.getCourse(this, classId);
            fillCourseForm(course);
        }

        setupDatePickers();
    }

    private void setupDatePickers() {
        etCourseStart.setOnClickListener(this);
        etCourseEnd.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        courseStartDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etCourseStart.setText(DateUtil.dateFormat.format(newDate.getTime()));
            }

        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        courseEndDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etCourseEnd.setText(DateUtil.dateFormat.format(newDate.getTime()));
            }

        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        etCourseStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    courseStartDateDialog.show();
            }
        });

        etCourseEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    courseEndDateDialog.show();
            }
        });
    }

    private void findViews() {
        etCourseName = (EditText) findViewById(R.id.etCourseName);
        etCourseStart = (EditText) findViewById(R.id.etCourseStart);
        etCourseEnd = (EditText) findViewById(R.id.etCourseEnd);
        etCourseMentor = (EditText) findViewById(R.id.etCourseMentor);
        etCourseMentorEmail = (EditText) findViewById(R.id.etCourseMentorEmail);
        etCourseMentorPhone = (EditText) findViewById(R.id.etCourseMentorPhone);
    }

    private void fillCourseForm(Course c) {
        etCourseName.setText(c.courseName);
        etCourseStart.setText(c.courseStart);
        etCourseEnd.setText(c.courseEnd);
        etCourseMentor.setText(c.courseMentor);
        etCourseMentorEmail.setText(c.courseMentorEmail);
        etCourseMentorPhone.setText(c.courseMentorPhone);
    }

    public void saveCourseChanges(View view) {
        if (action == Intent.ACTION_INSERT) {
            long termId = Long.parseLong(termUri.getLastPathSegment());
            DataManager.insertCourse(this, termId,
                    etCourseName.getText().toString().trim(),
                    etCourseStart.getText().toString().trim(),
                    etCourseEnd.getText().toString().trim(),
                    etCourseMentor.getText().toString().trim(),
                    etCourseMentorEmail.getText().toString().trim(),
                    etCourseMentorPhone.getText().toString().trim(),
                    CourseStatus.PLAN_TO_TAKE
            );

            setResult(RESULT_OK);
        }
        else if (action == Intent.ACTION_EDIT) {
            course.courseName = etCourseName.getText().toString().trim();
            course.courseStart = etCourseStart.getText().toString().trim();
            course.courseEnd = etCourseEnd.getText().toString().trim();
            course.courseMentor = etCourseMentor.getText().toString().trim();
            course.courseMentorEmail = etCourseMentorEmail.getText().toString().trim();
            course.courseMentorPhone = etCourseMentorPhone.getText().toString().trim();

            course.saveChanges(this);
            setResult(RESULT_OK);
        }

        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == etCourseStart) {
            courseStartDateDialog.show();
        }
        if (v == etCourseEnd) {
            courseEndDateDialog.show();
        }
    }
}
