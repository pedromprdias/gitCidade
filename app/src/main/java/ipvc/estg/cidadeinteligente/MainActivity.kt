package ipvc.estg.cidadeinteligente

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed(Runnable {
            val start = Intent(this@MainActivity, MenuInicio::class.java)
            startActivity(start)
            finish()
        }, 3000)
    }


}