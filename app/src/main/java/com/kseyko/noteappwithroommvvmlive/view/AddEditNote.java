package com.kseyko.noteappwithroommvvmlive.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kseyko.noteappwithroommvvmlive.R;

public class AddEditNote extends AppCompatActivity {
    public static final String EXTRA_ID = "NOTE_ID";
    public static final String EXTRA_TITLE = "NOTE_TITLE";
    public static final String EXTRA_DESCRIPTION = "NOTE_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "NOTE_PRIORITY";

    private EditText mEdtTitle, mEditDesc;
    private NumberPicker mEditPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);
        initView();
    }

    private void initView() {
        mEdtTitle = findViewById(R.id.edtTitle);
        mEditDesc = findViewById(R.id.edtDescription);
        mEditPriority = findViewById(R.id.npPriority);
        mEditPriority.setMinValue(0);
        mEditPriority.setMaxValue(10);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_2);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            mEdtTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            mEditDesc.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            mEditPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 0));
        } else {
            setTitle("Add Note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuAddNote:
                addNote();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNote() {
        String title = mEdtTitle.getText().toString().trim();
        String description = mEditDesc.getText().toString().trim();
        int priority = mEditPriority.getValue();

        if (title.isEmpty() && description.isEmpty()) {
            Toast.makeText(this, "Title or Description must be input.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_PRIORITY, priority);

        int noteId = getIntent().getIntExtra(EXTRA_ID, -1); // getIntent() # intent above
        if (noteId != -1) {
            intent.putExtra(EXTRA_ID, noteId);
            setResult(MainActivity.REQUEST_CODE_EDIT_NOTE, intent);
        } else {
            setResult(MainActivity.REQUEST_CODE_ADD_NOTE, intent);
        }
        finish();
    }
}