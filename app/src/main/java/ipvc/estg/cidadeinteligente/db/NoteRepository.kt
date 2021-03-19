package ipvc.estg.cidadeinteligente.db

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import ipvc.estg.cidadeinteligente.dao.NoteDao
import ipvc.estg.cidadeinteligente.entities.Notes

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Notes>> = noteDao.getAlphabetizedNotes()

    suspend fun insert(note: Notes) {
        noteDao.insert(note)
    }

    suspend fun deleteNote(id: Int) {
        noteDao.deleteNote(id)
    }

    suspend fun updateNote(id: Int, titulo :String, notes :String){
        noteDao.updateNote(id,titulo,notes)
    }

}
