package ipvc.estg.cidadeinteligente

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AddNote : AppCompatActivity() {

    private lateinit var editNoteView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        editNoteView = findViewById(R.id.add_note)

        val button = findViewById<Button>(R.id.button_add)
        button.setOnClickListener {
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editNoteView.text)){
                setResult(Activity.RESULT_OK, replyIntent)
            } else{
                val word = editNoteView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object{
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}