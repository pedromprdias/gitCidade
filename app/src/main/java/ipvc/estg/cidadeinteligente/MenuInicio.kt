package ipvc.estg.cidadeinteligente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuInicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_inicio)

        val button: Button = findViewById(R.id.buttonNota)
        button.setOnClickListener {
            val intent = Intent(this, ActivityNotes::class.java)
            startActivity(intent)
        }
    }
}