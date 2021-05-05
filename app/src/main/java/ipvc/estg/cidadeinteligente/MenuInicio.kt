package ipvc.estg.cidadeinteligente

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ipvc.estg.cidadeinteligente.api.EndPoints
import ipvc.estg.cidadeinteligente.api.ServiceBuilder
import ipvc.estg.cidadeinteligente.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest


class MenuInicio : AppCompatActivity() {
    private lateinit var usernameText: EditText
    private lateinit var passwordText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_inicio)

        fun sha256(input: String) = hashString("SHA-256", input)
        val intent = Intent(this, MapsActivity::class.java)

        val error:String = getString(R.string.error)

        val button: Button = findViewById(R.id.buttonNota)
        button.setOnClickListener {
            val intent = Intent(this, ActivityNotes::class.java)
            startActivity(intent)
        }

        usernameText = findViewById(R.id.editUser)
        passwordText = findViewById(R.id.editPassword)

        val buttonLogin: Button = findViewById(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            if(usernameText.length()==0){
                usernameText.setError(error)
            }
            if(passwordText.length()==0){
                passwordText.setError(error)
            }
            if(usernameText.length()!=0 && passwordText.length()!=0){
                val passEnc = passwordText.text.toString()
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val passRes = sha256(passEnc)
                val call = request.userLogin(usernameText.text.toString(), passRes)

                call.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@MenuInicio, "${t.message}", Toast.LENGTH_SHORT).show()
                    }


                })
            }
        }
    }

    private fun hashString(type: String, input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
                .getInstance(type)
                .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

}
