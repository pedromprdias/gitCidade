package ipvc.estg.cidadeinteligente

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddNote : AppCompatActivity() {

    private lateinit var editNoteView: EditText
    private lateinit var tituloNoteView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        editNoteView = findViewById(R.id.add_note)
        tituloNoteView = findViewById(R.id.titulo_add_note)

        val button = findViewById<Button>(R.id.button_add)
        button.setOnClickListener {
            val replyIntent = Intent()
            val error:String = getString(R.string.error)

            if(editNoteView.text.length == 0) {
                editNoteView.setError(error)
            }
            if(tituloNoteView.text.length == 0){
                tituloNoteView.setError(error)
            }
            if(tituloNoteView.text.length != 0 && editNoteView.text.length != 0){
                val word = editNoteView.text.toString()
                val title = tituloNoteView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_titulo, word)
                replyIntent.putExtra(EXTRA_REPLY_nota, title)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    companion object{
        const val EXTRA_REPLY_titulo = "com.example.android.wordlistsql.REPLY_titulo"
        const val EXTRA_REPLY_nota = "com.example.android.wordlistsql.REPLY_nota"
    }
}