package com.example.rifkinc196finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CourseViewerActivity extends AppCompatActivity {

    private TextView tvCourseName;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvStatus;
    private Uri courseUri;
    private long courseId;
    private Course course;
    private Menu menu;
    private static final int COURSE_NOTE_LIST_ACTIVITY_CODE = 11111;
    private static final int ASSESSMENT_LIST_ACTIVITY_CODE = 22222;
    private static final int COURSE_EDITOR_ACTIVITY_CODE = 33333;

    private void setStatusLabel() {
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        String status = "";

        switch(course.status.toString()) {
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

        tvStatus.setText("Status: " + status);
    }

    public void openClassNotesList(View view) {
        Intent intent = new Intent(CourseViewerActivity.this, CourseNoteListActivity.class);
        Uri uri = Uri.parse(DataProvider.COURSE_URI + "/" + courseId);
        intent.putExtra(DataProvider.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, COURSE_NOTE_LIST_ACTIVITY_CODE);
    }

    public void openAssessments(View view) {
        Intent intent = new Intent(CourseViewerActivity.this, AssessmentListActivity.class);
        Uri uri = Uri.parse(DataProvider.COURSE_URI + "/" + courseId);
        intent.putExtra(DataProvider.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, ASSESSMENT_LIST_ACTIVITY_CODE);
    }

    private void findElements() {
        tvCourseName = (TextView) findViewById(R.id.tvCourseName);
        tvStartDate = (TextView) findViewById(R.id.tvCourseStart);
        tvEndDate = (TextView) findViewById(R.id.tvCourseEnd);

        tvCourseName.setText(course.courseName);
        tvStartDate.setText(course.courseStart);
        tvEndDate.setText(course.courseEnd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_viewer, menu);
        this.menu = menu;
        showAppropriateMenuOptions();
        return true;
    }

    private void showAppropriateMenuOptions() {
        SharedPreferences sp = getSharedPreferences(AlarmHandler.courseAlarmFile, Context.MODE_PRIVATE);
        MenuItem item;

        menu.findItem(R.id.action_enable_notifications).setVisible(true);
        menu.findItem(R.id.action_disable_notifications).setVisible(true);

        if (course.courseNotifications) {
            item = menu.findItem(R.id.action_enable_notifications);
        } else {
            item = menu.findItem(R.id.action_disable_notifications);
        }

        if (course.status == null) {
            course.status = CourseStatus.PLAN_TO_TAKE;
            course.saveChanges(this);
        }

        switch (course.status.toString()) {
            case ("PLAN_TO_TAKE"):
                menu.findItem(R.id.action_drop_course).setVisible(false);
                menu.findItem(R.id.action_start_course).setVisible(true);
                menu.findItem(R.id.action_mark_course_completed).setVisible(false);
                break;

            case ("COMPLETED"):
                menu.findItem(R.id.action_drop_course).setVisible(false);
                menu.findItem(R.id.action_start_course).setVisible(false);
                menu.findItem(R.id.action_mark_course_completed).setVisible(false);
                break;

            case ("IN_PROGRESS"):
                menu.findItem(R.id.action_drop_course).setVisible(true);
                menu.findItem(R.id.action_start_course).setVisible(false);
                menu.findItem(R.id.action_mark_course_completed).setVisible(true);
                break;

            case ("DROPPED"):
                menu.findItem(R.id.action_drop_course).setVisible(false);
                menu.findItem(R.id.action_start_course).setVisible(false);
                menu.findItem(R.id.action_mark_course_completed).setVisible(false);
                break;
        }

        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_edit_course:
                return editCourse();
            case R.id.action_delete_course:
                return deleteCourse();
            case R.id.action_enable_notifications:
                return enableNotifications();
            case R.id.action_disable_notifications:
                return disableNotifications();
            case R.id.action_drop_course:
                return dropCourse();
            case R.id.action_start_course:
                return startCourse();
            case R.id.action_mark_course_completed:
                return markCourseComplete();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean editCourse() {
        Intent intent = new Intent(this, CourseEditorActivity.class);
        Uri uri = Uri.parse(DataProvider.COURSE_URI + "/" + course.courseId);
        intent.putExtra(DataProvider.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, COURSE_EDITOR_ACTIVITY_CODE);
        return true;
    }

    private boolean dropCourse() {
        course.status = CourseStatus.DROPPED;
        course.saveChanges(this);
        setStatusLabel();
        showAppropriateMenuOptions();
        return true;
    }

    private boolean startCourse() {
        course.status = CourseStatus.IN_PROGRESS;
        course.saveChanges(this);
        setStatusLabel();
        showAppropriateMenuOptions();
        return true;
    }

    private boolean markCourseComplete() {
        course.status = CourseStatus.COMPLETED;
        course.saveChanges(this);
        setStatusLabel();
        showAppropriateMenuOptions();
        return true;
    }

    private boolean disableNotifications() {
        course.courseNotifications = false;
        course.saveChanges(this);
        showAppropriateMenuOptions();
        return true;
    }

    private boolean enableNotifications() {
        long now = DateUtil.todayLong();
        Long tsLong = System.currentTimeMillis();

        if (now <= DateUtil.getDateTimestamp(course.courseStart)) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseStart), "Course starts Today!", course.courseName + " begins on " + course.courseStart);
        }
        if (now <= DateUtil.getDateTimestamp(course.courseStart) - 3 * 24 * 60 * 60 * 1000) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseStart) - 3 * 24 * 60 * 60 * 1000, "Course starts in 3 days", course.courseName + " begins on " + course.courseStart);
        }
        if (now <= DateUtil.getDateTimestamp(course.courseStart) - 24 * 60 * 60 * 1000) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseStart) - 24 * 60 * 60 * 1000, "Course starts tomorrow", course.courseName + " begins on " + course.courseStart);
        }

        if (now <= DateUtil.getDateTimestamp(course.courseEnd)) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseEnd), "Course ends Today!", course.courseName + " ends on " + course.courseEnd);
        }
        if (now <= DateUtil.getDateTimestamp(course.courseEnd) - 3 * 24 * 60 * 60 * 1000) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseEnd) - 3 * 24 * 60 * 60 * 1000, "Course ends in 3 days", course.courseName + " ends on " + course.courseEnd);
        }
        if (now <= DateUtil.getDateTimestamp(course.courseEnd) - 24 * 60 * 60 * 1000) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseEnd) - 24 * 60 * 60 * 1000, "Course ends tomorrow", course.courseName + " ends on " + course.courseEnd);
        }

        course.courseNotifications = true;
        course.saveChanges(this);
        showAppropriateMenuOptions();

        return true;
    }

    private boolean deleteCourse() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            DataManager.deleteCourse(CourseViewerActivity.this, courseId);
                            setResult(RESULT_OK);
                            finish();

                            Toast.makeText(CourseViewerActivity.this,
                                    R.string.course_deleted,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_course)
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
        courseId = Long.parseLong(courseUri.getLastPathSegment());
        course = DataManager.getCourse(this, courseId);

        setStatusLabel();
        findElements();
    }
}
