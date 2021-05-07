package ipvc.estg.cidadeinteligente

import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import ipvc.estg.cidadeinteligente.api.EndPoints
import ipvc.estg.cidadeinteligente.api.ReportOutpost
import ipvc.estg.cidadeinteligente.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_add_report.*
import kotlinx.android.synthetic.main.activity_info_marker.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoMarker : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_marker)

        val error:String = getString(R.string.error)

        val id = intent.getIntExtra("id",0)
        val title = intent.getStringExtra("titleInt")
        val desc = intent.getStringExtra("descInt")

        editTitle.setText(title.toString())

        editDescription.setText(desc.toString())

        editRButon.setOnClickListener {

            if(editTitle.text.length == 0){
                editTitle.setError(error)
            }

            if(editDescription.text.length == 0){
                editDescription.setError(error)
            }
            if(editTitle.text.length != 0 && editDescription.text.length != 0) {
                val titleEdited = editTitle.text.toString()
                val descriptionEdited = editDescription.text.toString()

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.editReport(id, titleEdited, descriptionEdited)

                call.enqueue(object :Callback<ReportOutpost>{
                    override fun onResponse(call: Call<ReportOutpost>, response: Response<ReportOutpost>) {
                        if(response.isSuccessful) {
                            val intent = Intent(this@InfoMarker, MapsActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<ReportOutpost>, t: Throwable) {
                        val intent = Intent(this@InfoMarker, MapsActivity::class.java)
                        startActivity(intent)
                    }

                })
            }
        }

        cancelR.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

}