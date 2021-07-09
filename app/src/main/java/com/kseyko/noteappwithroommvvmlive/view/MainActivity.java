package com.kseyko.noteappwithroommvvmlive.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kseyko.noteappwithroommvvmlive.R;
import com.kseyko.noteappwithroommvvmlive.adapter.NoteAdapter;
import com.kseyko.noteappwithroommvvmlive.model.Note;
import com.kseyko.noteappwithroommvvmlive.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.IOnClickListener {

    public static final int REQUEST_CODE_ADD_NOTE = 69;
    public static final int REQUEST_CODE_EDIT_NOTE = 113;

    private FloatingActionButton mFbAddNote;
    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;
    private NoteViewModel mNoteViewModel;
    private List<Note> mListNotes;
    ActivityResultLauncher<Intent> intentLaunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }


    private void initView() {
        mRecyclerView = findViewById(R.id.rvNote);
        mFbAddNote = findViewById(R.id.fbAddNote);
        mRecyclerView.setHasFixedSize(true);
        mNoteAdapter= new NoteAdapter();
        mRecyclerView.setAdapter(mNoteAdapter);
        mListNotes= new ArrayList<>();

        mNoteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, notes -> {
            Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
            mListNotes.clear();
            mListNotes.addAll(notes);
            mNoteAdapter.submitList(notes);
        });

    }
    private void initEvent() {
        itemTouchHelper.attachToRecyclerView(mRecyclerView);// setup for case swipe to delete note item
        mFbAddNote.setOnClickListener(view -> {
            addNote();
        });
        mNoteAdapter.setOnClickListener(this);

        intentLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == REQUEST_CODE_ADD_NOTE ) {
                        // There are no request codes
                        String title = result.getData().getStringExtra(AddEditNote.EXTRA_TITLE);
                        String description = result.getData().getStringExtra(AddEditNote.EXTRA_DESCRIPTION);
                        int priority = result.getData().getIntExtra(AddEditNote.EXTRA_PRIORITY, 0);

                        Note note = new Note(title, description, priority);
                        mNoteViewModel.insert(note);

                    }else if (result.getResultCode()== REQUEST_CODE_EDIT_NOTE){
                        int noteId = result.getData().getIntExtra(AddEditNote.EXTRA_ID, -1);
                        if (noteId == -1) {
                            Toast.makeText(MainActivity.this, "can't update note", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String title = result.getData().getStringExtra(AddEditNote.EXTRA_TITLE);
                        String description = result.getData().getStringExtra(AddEditNote.EXTRA_DESCRIPTION);
                        int priority = result.getData().getIntExtra(AddEditNote.EXTRA_PRIORITY, 0);

                        Note note = new Note(title, description, priority);
                        note.setId(noteId);
                        mNoteViewModel.update(note);
                        Toast.makeText(MainActivity.this, "Note edited", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addNote() {
        Intent intent = new Intent(this, AddEditNote.class);
        intentLaunch.launch(intent);
    }

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            mNoteViewModel.delete(mNoteAdapter.getNoteAt(position));
            Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    public void onItemViewClick(Note note) {
        Intent intent= new Intent(MainActivity.this,AddEditNote.class);
        intent.putExtra(AddEditNote.EXTRA_TITLE, note.getTitle());
        intent.putExtra(AddEditNote.EXTRA_DESCRIPTION, note.getDescription());
        intent.putExtra(AddEditNote.EXTRA_PRIORITY, note.getPriority());
        intent.putExtra(AddEditNote.EXTRA_ID, note.getId());
        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        intentLaunch.launch(intent);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuDeleteAllNote) {
            deleteAllNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNote() {
        mNoteViewModel.deleteAllNotes();
        Toast.makeText(MainActivity.this, "All notes deleted", Toast.LENGTH_SHORT).show();
    }
}