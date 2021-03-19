package ipvc.estg.cidadeinteligente.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.cidadeinteligente.ActivityNotes
import ipvc.estg.cidadeinteligente.dao.NoteDao
import ipvc.estg.cidadeinteligente.entities.Notes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Notes::class), version = 5, exportSchema = false)
public abstract class NoteDB() : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    private class WordDataBaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var noteDao = database.noteDao()
                }
            }
        }

    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NoteDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDB::class.java,
                    "notes_database"
                )
                    //.fallbackToDestructiveMigration()
                    .addCallback(WordDataBaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}


