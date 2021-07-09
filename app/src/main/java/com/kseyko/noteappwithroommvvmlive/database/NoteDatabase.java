package com.kseyko.noteappwithroommvvmlive.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kseyko.noteappwithroommvvmlive.model.Note;

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
@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase mInstance;

    public static synchronized NoteDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context, NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(mRoomCallback)
                    .build();
        }
        return mInstance;
    }

    private static RoomDatabase.Callback mRoomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopularDbTask(mInstance).execute();
        }
    };

    private static class PopularDbTask extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;

        public PopularDbTask(NoteDatabase database) {
            noteDao=database.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "My Description 1", 1));
            noteDao.insert(new Note("Title 2", "My Description 2", 2));
            noteDao.insert(new Note("Title 3", "My Description 3", 3));
            return null;
        }
    }

    public abstract NoteDao noteDao();
}
