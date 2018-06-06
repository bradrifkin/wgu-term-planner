package com.example.rifkinc196finalproject;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

    private static final int TERM_LIST_ACTIVITY_CODE = 11111;
    private static final int TERM_VIEWER_ACTIVITY_CODE = 22222;

    public void openTermList(View view) {
        Intent intent = new Intent(this, TermListActivity.class);
        startActivityForResult(intent, TERM_LIST_ACTIVITY_CODE);
    }

    public void openCurrentTerm(View view) {
        Cursor c = getContentResolver().query(DataProvider.TERM_URI, null, DBOpenHelper.TERM_ACTIVE + " = 1", null, null);
        while (c.moveToNext()) {
            Intent intent = new Intent(this, TermViewerActivity.class);
            long id = c.getLong(c.getColumnIndex(DBOpenHelper.TERM_TABLE_ID));
            Uri uri = Uri.parse(DataProvider.TERM_URI + "/" + id);
            intent.putExtra(DataProvider.TERM_CONTENT_TYPE, uri);
            startActivityForResult(intent, TERM_VIEWER_ACTIVITY_CODE);
            return;
        }

        Toast.makeText(this,
                "No term has been marked active. Set an active term from any term page.",
                Toast.LENGTH_SHORT).show();
    }

    private boolean createSampleData() {
        Uri term1Uri = DataManager.insertTerm(this, "Term 1", "2017-10-01", "2018-03-31", 0);
        Uri term2Uri = DataManager.insertTerm(this, "Term 2", "2018-04-01", "2018-09-30", 1);
        Uri term3Uri = DataManager.insertTerm(this, "Term 3", "2018-10-01", "2019-03-31", 0);
        Uri term4Uri = DataManager.insertTerm(this, "Term 4", "2019-04-01", "2019-09-30", 0);

        Uri course1Uri = DataManager.insertCourse(this, Long.parseLong(term2Uri.getLastPathSegment()),
                "C482: Software I", "2018-04-01", "2018-04-14",
                "Course Instructor Group", "cmsoftware@wgu.edu", "(801) 274-3280",
                CourseStatus.COMPLETED);

        Uri course2Uri = DataManager.insertCourse(this, Long.parseLong(term2Uri.getLastPathSegment()),
                "C196: Mobile Application Development", "2018-04-15", "2018-05-31",
                "Pubali Banerjee", "pubali.banerjee@wgu.edu", "(801) 924-4710",
                CourseStatus.IN_PROGRESS);

        Uri course3Uri = DataManager.insertCourse(this, Long.parseLong(term1Uri.getLastPathSegment()),
                "C393: IT Foundations", "2017-11-01", "2017-12-05",
                "Course Instructor Group", "cmitfund1@wgu.edu", "(801) 274-3280",
                CourseStatus.COMPLETED);

        Uri course4Uri = DataManager.insertCourse(this, Long.parseLong(term1Uri.getLastPathSegment()),
                "C777: Web Development Applications", "2017-12-27", "2018-01-20",
                "Course Instructor Group", "cmweb@wgu.edu", "(801) 274-3280",
                CourseStatus.COMPLETED);

        Uri course5Uri = DataManager.insertCourse(this, Long.parseLong(term2Uri.getLastPathSegment()),
                "C769: IT Capstone Written Project", "2018-06-01", "2018-06-30",
                "Course Instructor Group", "ugcapstoneit@wgu.edu", "(801) 274-3280",
                CourseStatus.PLAN_TO_TAKE);

        DataManager.insertCourseNote(this, Long.parseLong(course1Uri.getLastPathSegment()),
                "Sample short note for C482");

        DataManager.insertCourseNote(this, Long.parseLong(course1Uri.getLastPathSegment()),
                getString(R.string.long_test_note));

        Uri ass1Uri = DataManager.insertAssessment(this, Long.parseLong(course1Uri.getLastPathSegment()), "GYP1",
                "Performance Assessment: Software I", "Throughout your career in software design and development, you will be asked to create applications with various features and functionality based on business requirements. When a new system is developed, typically the process begins with a business analyst gathering and writing these business requirements, with the assistance of subject matter experts from the business. Then a system analyst works with several application team members and others to formulate a solution based on the requirements. As a developer, you would then create a design document from the solution and finally develop the system based on your design document.\n" +
                        "\n" +
                        "For this assessment, you will create a Java application using the solution statements provided in the requirements section.\n" +
                        "\n" +
                        "Your submission should include a zip file with all the necessary code files to compile, support, and run your application.\n", "2018-04-06 09:55:00 AM");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass1Uri.getLastPathSegment()),
                "1st sample note for Assessment #1");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass1Uri.getLastPathSegment()),
                "2nd sample note for Assessment #1");


        Toast.makeText(MainActivity.this,
                getString(R.string.add_test_data),
                Toast.LENGTH_SHORT).show();
        return true;
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {
    }

    private boolean deleteAllData() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            getContentResolver().delete(DataProvider.TERM_URI, null, null);
                            getContentResolver().delete(DataProvider.COURSE_URI, null, null);
                            getContentResolver().delete(DataProvider.COURSE_NOTE_URI, null, null);
                            getContentResolver().delete(DataProvider.ASSESSMENT_URI, null, null);
                            getContentResolver().delete(DataProvider.ASSESSMENT_NOTE_URI, null, null);
                            restartLoader();

                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

        return true;
    }

    public void handleCreateSampleData(View view) {
        createSampleData();
    }

    public void handleDeleteAllData(View view) {
        deleteAllData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}
