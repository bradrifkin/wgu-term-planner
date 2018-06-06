package com.example.rifkinc196finalproject;

import android.content.ContentValues;
import android.content.Context;

public class AssessmentNote {

    public String text;
    private long assessmentNoteId;
    public long assessmentId;

    public AssessmentNote(long assessmentNoteId) {
        this.assessmentNoteId = assessmentNoteId;
    }

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_NOTE_TEXT, text);
        values.put(DBOpenHelper.ASSESSMENT_NOTE_ASSESSMENT_ID, assessmentId);

        context.getContentResolver().update(DataProvider.ASSESSMENT_NOTE_URI, values, DBOpenHelper.ASSESSMENT_NOTE_TABLE_ID + "=" + assessmentNoteId, null);
    }

}
