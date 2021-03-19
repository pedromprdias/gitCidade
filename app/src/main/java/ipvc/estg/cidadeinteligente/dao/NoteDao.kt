package ipvc.estg.cidadeinteligente.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.cidadeinteligente.entities.Notes

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table ORDER BY notes ASC")
    fun getAlphabetizedNotes(): LiveData<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Notes)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Query("DELETE FROM note_table WHERE id = :id")
    fun deleteNote(id :Int)

}