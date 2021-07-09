package com.kseyko.noteappwithroommvvmlive.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kseyko.noteappwithroommvvmlive.model.Note;
import com.kseyko.noteappwithroommvvmlive.repository.NoteRepository;

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
public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(Application application) {
        super(application);
        this.noteRepository = new NoteRepository(application);
        this.allNotes = noteRepository.getAllNotes();
    }

    public void insert(Note note){
        noteRepository.insert (note);
    }
    public void update(Note note){
        noteRepository.update(note);
    }
    public void delete(Note note){
        noteRepository.delete(note);

    }
    public void deleteAllNotes(){
        noteRepository.deleteAllNotes();
    }
//    public LiveData<List<Note>> getNotes(){
//        return noteRepository.getAllNotes();
//    }
    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
}
