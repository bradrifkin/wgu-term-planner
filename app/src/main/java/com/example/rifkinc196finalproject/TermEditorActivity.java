package com.example.rifkinc196finalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TermEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private DataProvider db;
    private DatePickerDialog termStartDateDialog;
    private DatePickerDialog termEndDateDialog;
    private SimpleDateFormat dateFormatter;
    private static final int MAIN_ACTIVITY_CODE = 1;
    private String action;
    private String termFilter;
    private Term term;
    private EditText termNameField;
    private EditText termStartDateField;
    private EditText termEndDateField;

    private void getTermFromForm() {
        term.termName = termNameField.getText().toString().trim();
        term.termStartDate = termStartDateField.getText().toString().trim();
        term.termEndDate = termEndDateField.getText().toString().trim();
    }

    private void fillTermForm(Term t) {
        termNameField.setText(t.termName);
        termStartDateField.setText(t.termStartDate);
        termEndDateField.setText(t.termEndDate);
    }

    private void setupDatePickers() {
        termStartDateField.setOnClickListener(this);
        termEndDateField.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        termStartDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                termStartDateField.setText(dateFormatter.format(newDate.getTime()));
            }

        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        termEndDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                termEndDateField.setText(dateFormatter.format(newDate.getTime()));
            }

        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        termStartDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    termStartDateDialog.show();
            }
        });

        termEndDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    termEndDateDialog.show();
            }
        });
    }

    public void saveTermChanges(View view) {
        if (action == Intent.ACTION_INSERT) {
            term = new Term();
            getTermFromForm();

            DataManager.insertTerm(this,
                    term.termName,
                    term.termStartDate,
                    term.termEndDate,
                    term.active
            );

            Toast.makeText(this,
                    getString(R.string.term_saved),
                    Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);

        } else if (action == Intent.ACTION_EDIT) {
            getTermFromForm();
            term.saveChanges(this);

            Toast.makeText(this,
                    getString(R.string.term_updated),
                    Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == termStartDateField) {
            termStartDateDialog.show();
        }
        if (v == termEndDateField) {
            termEndDateDialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DataProvider();

        termNameField = (EditText) findViewById(R.id.termNameEditText);
        termStartDateField = (EditText) findViewById(R.id.termStartDateEditText);
        termStartDateField.setInputType(InputType.TYPE_NULL);
        termEndDateField = (EditText) findViewById(R.id.termEndDateEditText);
        termEndDateField.setInputType(InputType.TYPE_NULL);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.add_new_term));
        } else {
            action = Intent.ACTION_EDIT;
            setTitle("Edit Term");
            long termId = Long.parseLong(uri.getLastPathSegment());
            term = DataManager.getTerm(this, termId);
            fillTermForm(term);
        }

        setupDatePickers();
    }
}