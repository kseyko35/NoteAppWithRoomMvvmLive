package com.kseyko.noteappwithroommvvmlive.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import com.kseyko.noteappwithroommvvmlive.database.NoteDao;
import com.kseyko.noteappwithroommvvmlive.database.NoteDatabase;
import com.kseyko.noteappwithroommvvmlive.model.Note;

import java.util.List;

/**
 * Code with ❤
 * ╔════════════════════════════╗
 * ║   Created by Seyfi ERCAN   ║
 * ╠════════════════════════════╣
 * ║  seyfiercan35@hotmail.com  ║
 * ╠════════════════════════════╣
 * ║      30,June,2021      ║
 * ╚════════════════════════════╝
 */
public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao= database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note){
        new InsertNoteTask(noteDao).execute(note);
    }
    public void update(Note note){
        new UpdateNoteTask(noteDao).execute(note);
    }
    public void delete(Note note){
        new DeleteNoteTask(noteDao).execute(note);

    }
    public void deleteAllNotes(){
        new DeleteNoteAllTasks(noteDao).execute();
    }
    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
//    public LiveData<List<Note>> getAllNotes(){
//        return noteDao.getAllNotes();
//    }


    private static class InsertNoteTask extends AsyncTask<Note,Void,Void> {
        private NoteDao noteDao;
        public InsertNoteTask(NoteDao noteDao) {
            this.noteDao= noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }
    private static class UpdateNoteTask extends AsyncTask<Note,Void,Void>{
        private NoteDao noteDao;
        public UpdateNoteTask(NoteDao noteDao) {
            this.noteDao= noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }
    private static class DeleteNoteTask extends AsyncTask<Note,Void,Void>{
        private NoteDao noteDao;
        public DeleteNoteTask(NoteDao noteDao) {
            this.noteDao= noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
    private static class DeleteNoteAllTasks extends AsyncTask<Note,Void,Void>{
        private NoteDao noteDao;
        public DeleteNoteAllTasks(NoteDao noteDao) {
            this.noteDao= noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteAll();
            return null;
        }
    }
}
