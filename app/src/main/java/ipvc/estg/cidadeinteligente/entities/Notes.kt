package ipvc.estg.cidadeinteligente.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")

class Notes (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "notes") val notes: String
)