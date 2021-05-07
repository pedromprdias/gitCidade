package ipvc.estg.cidadeinteligente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class InfoMarker : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_marker)

        val yo = intent.getStringExtra("titleInt")

        val a = findViewById<TextView>(R.id.shesh)

        a.setText(yo)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

}