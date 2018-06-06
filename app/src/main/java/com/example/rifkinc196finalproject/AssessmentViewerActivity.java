package com.example.rifkinc196finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AssessmentViewerActivity extends AppCompatActivity {

    private long assessmentId;
    private static final int ASSESSMENT_EDITOR_ACTIVITY_CODE = 11111;
    private static final int ASSESSMENT_NOTE_LIST_ACTIVITY_CODE = 22222;
    private Assessment assessment;
    private Menu menu;
    private TextView tvAssessmentTitle;
    private TextView tvAssessmentDesc;
    private TextView tvAssessmentDateTime;

    private void loadAssessment() {
        Uri assessmentUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_CONTENT_TYPE);
        assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
        assessment = DataManager.getAssessment(this, assessmentId);

        tvAssessmentTitle = (TextView) findViewById(R.id.tvAssessmentTitle);
        tvAssessmentDesc = (TextView) findViewById(R.id.tvAssessmentDesc);
        tvAssessmentDateTime = (TextView) findViewById(R.id.tvAssessmentDateTime);

        tvAssessmentTitle.setText(assessment.code + ": " + assessment.name);
        tvAssessmentDesc.setText(assessment.description);
        tvAssessmentDateTime.setText(assessment.dateTime);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadAssessment();
        }
    }

    public void openAssessmentNotesList(View view) {
        Intent intent = new Intent(AssessmentViewerActivity.this, AssessmentNoteListActivity.class);
        Uri uri = Uri.parse(DataProvider.ASSESSMENT_URI + "/" + assessmentId);
        intent.putExtra(DataProvider.ASSESSMENT_CONTENT_TYPE, uri);
        startActivityForResult(intent, ASSESSMENT_NOTE_LIST_ACTIVITY_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_viewer, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_assessment:
                return deleteAssessment();
            case R.id.action_enable_notifications:
                return enableNotifications();
            case R.id.action_disable_notifications:
                return disableNotifications();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAppropriateMenuOptions() {
//        SharedPreferences sp = getSharedPreferences(AlarmHandler.courseAlarmFile, Context.MODE_PRIVATE);
//        MenuItem item;

        menu.findItem(R.id.action_enable_notifications).setVisible(true);
        menu.findItem(R.id.action_disable_notifications).setVisible(true);

        if (assessment.assessmentNotifications == 1) {
            menu.findItem(R.id.action_enable_notifications).setVisible(false);
        } else {
            menu.findItem(R.id.action_disable_notifications).setVisible(false);
        }
    }

    private boolean disableNotifications() {
        assessment.assessmentNotifications = 0;
        assessment.saveChanges(this);
        showAppropriateMenuOptions();
        return true;
    }

    private boolean enableNotifications() {
        long now = DateUtil.todayLong();
//        Long tsLong = System.currentTimeMillis();

        AlarmHandler.scheduleAssessmentAlarm(getApplicationContext(), (int) assessmentId, System.currentTimeMillis() + 1000, "Assessment is today!", assessment.name + " takes place on " + assessment.dateTime);

        if (now <= DateUtil.getDateTimestamp(assessment.dateTime)) {
            AlarmHandler.scheduleAssessmentAlarm(getApplicationContext(), (int) assessmentId, DateUtil.getDateTimestamp(assessment.dateTime), "Assessment is today!", assessment.name + " takes place on " + assessment.dateTime);
        }
        if (now <= DateUtil.getDateTimestamp(assessment.dateTime) - 3 * 24 * 60 * 60 * 1000) {
            AlarmHandler.scheduleAssessmentAlarm(getApplicationContext(), (int) assessmentId, DateUtil.getDateTimestamp(assessment.dateTime) - 3 * 24 * 60 * 60 * 1000, "Assessment in 3 days", assessment.name + " takes place on " + assessment.dateTime);
        }
        if (now <= DateUtil.getDateTimestamp(assessment.dateTime) - 24 * 60 * 60 * 1000) {
            AlarmHandler.scheduleAssessmentAlarm(getApplicationContext(), (int) assessmentId, DateUtil.getDateTimestamp(assessment.dateTime) - 24 * 60 * 60 * 1000, "Assessment is tomorrow", assessment.name + " takes place on " + assessment.dateTime);
        }

        assessment.assessmentNotifications = 1;
        assessment.saveChanges(this);
        showAppropriateMenuOptions();

        return true;
    }

    private boolean deleteAssessment() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            DataManager.deleteAssessment(AssessmentViewerActivity.this, assessmentId);
                            setResult(RESULT_OK);
                            finish();

                            Toast.makeText(AssessmentViewerActivity.this,
                                    R.string.assessment_deleted,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_assessment)
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentViewerActivity.this, AssessmentEditorActivity.class);
                Uri uri = Uri.parse(DataProvider.ASSESSMENT_URI + "/" + assessmentId);
                intent.putExtra(DataProvider.ASSESSMENT_CONTENT_TYPE, uri);
                startActivityForResult(intent, ASSESSMENT_EDITOR_ACTIVITY_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadAssessment();
    }
}
