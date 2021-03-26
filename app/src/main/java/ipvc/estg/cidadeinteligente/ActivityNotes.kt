package ipvc.estg.cidadeinteligente

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cidadeinteligente.entities.Notes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cidadeinteligente.adapter.NoteAdapter
import ipvc.estg.cidadeinteligente.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.alert_edit.view.*
import kotlinx.android.synthetic.main.recyclerline.*


class ActivityNotes : AppCompatActivity(),NoteAdapter.OnDeleteClickListener, NoteAdapter.OnUpdateClickListener{

    private lateinit var noteViewModel: NoteViewModel
    private val newNoteActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = NoteAdapter(this, this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes?.let{ adapter.setNotes(it)}
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
            val titulo = data?.getStringExtra(AddNote.EXTRA_REPLY_titulo).toString()
            val nota = data?.getStringExtra(AddNote.EXTRA_REPLY_nota).toString()

            val note = Notes(notes = nota,titulo = titulo)
                noteViewModel.insert(note)


        }
    }


    override fun onDeleteClick(id: Int) {
        val yes: String = getString(R.string.yes)
        val no: String = getString(R.string.no)
        val title: String = getString(R.string.title)
        val dia:String = getString(R.string.confirm)

        val builder = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(dia)

        builder.setPositiveButton(yes,DialogInterface.OnClickListener { dialog, with ->
            noteViewModel.deleteNote(id)
            dialog.cancel()
        })

        builder.setNegativeButton(no, DialogInterface.OnClickListener { dialog, with ->
            dialog.cancel()
        })

        val alert = builder.create()
        alert.show()

    }

    override fun onUpdateClick(id: Int, titulo: String, notes: String) {
        val title: String = getString(R.string.edit)

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_edit, null)

        val mBuiler = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(title)

        mDialogView.titulo_edit.setText(titulo)
        mDialogView.nota_edit.setText(notes)

        val mAlertDialog = mBuiler.show()

        mDialogView.save.setOnClickListener {
            val tituloUpdated = mDialogView.titulo_edit.text.toString()
            val notesUpdated = mDialogView.nota_edit.text.toString()

            val error:String = getString(R.string.error)

            if(tituloUpdated.length == 0) {
                mDialogView.titulo_edit.setError(error)
            }
            if(notesUpdated.length == 0){
                mDialogView.nota_edit.setError(error)
            }
            if(tituloUpdated.length != 0 && notesUpdated.length != 0){
                mAlertDialog.dismiss()
                noteViewModel.updateNote(id, tituloUpdated, notesUpdated)
            }
        }

        mDialogView.cancel.setOnClickListener {
            mAlertDialog.dismiss()
        }

    }

}