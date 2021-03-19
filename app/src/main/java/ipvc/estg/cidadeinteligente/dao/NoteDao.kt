package ipvc.estg.cidadeinteligente.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.cidadeinteligente.entities.Notes

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table")
    fun getAlphabetizedNotes(): LiveData<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Notes)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Query("DELETE FROM note_table WHERE id = :id")
    suspend fun deleteNote(id :Int)

    @Query("UPDATE note_table SET titulo = :titulo, notes = :notes WHERE id =:id")
    suspend fun updateNote(id:Int, titulo:String, notes: String)

}