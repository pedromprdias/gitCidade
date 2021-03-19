package ipvc.estg.cidadeinteligente

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cidadeinteligente.entities.Notes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cidadeinteligente.adapter.NoteAdapter
import ipvc.estg.cidadeinteligente.db.NoteDB
import ipvc.estg.cidadeinteligente.viewmodel.NoteViewModel


class ActivityNotes : AppCompatActivity(){

    private lateinit var noteViewModel: NoteViewModel
    private val newNoteActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = NoteAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes.let{ adapter.setNotes(it)}
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK){
            data?.getStringExtra(AddNote.EXTRA_REPLY)?.let {
                val note = Notes(notes = it)
                noteViewModel.insert(note)
            }
        } else{
            Toast.makeText(
                applicationContext,
                "Nota nao inserida",
                Toast.LENGTH_LONG).show()
        }
    }


}