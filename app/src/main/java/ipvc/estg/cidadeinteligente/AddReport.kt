package ipvc.estg.cidadeinteligente

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import ipvc.estg.cidadeinteligente.api.EndPoints
import ipvc.estg.cidadeinteligente.api.ReportOutpost
import ipvc.estg.cidadeinteligente.api.ServiceBuilder
import ipvc.estg.cidadeinteligente.api.User
import kotlinx.android.synthetic.main.activity_add_report.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.*

class AddReport : AppCompatActivity() {
    private val IMAGE_CAPTURE_CODE = 1001
    private val PERMISSION_CODE = 1000
    var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report)

        val error:String = getString(R.string.error)

        val spinner: Spinner = findViewById(R.id.spinner)

        val adapter = ArrayAdapter.createFromResource(this,R.array.types,android.R.layout.simple_list_item_1)
        spinner.adapter=adapter

        val latPassed = intent.getStringExtra("lat").toDouble()
        val lngPassed = intent.getStringExtra("lng").toDouble()

        val desc = descricaoReport.text.toString()

        val sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE)
        var userName = sharedPreferences.getString("userPref","defaultName").toString()

        takeP.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.CAMERA)
                        ==PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){

                    val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    openCamera()
                }
            }
            else{
                openCamera()
            }
        }

        getCurrent.setOnClickListener {
            latEdit.setText(latPassed.toString())
            lngEdit.setText(lngPassed.toString())
        }

        addReport.setOnClickListener {
            if(latEdit.text.length == 0){
                latEdit.setError(error)
            }
            if(lngEdit.text.length == 0){
                lngEdit.setError(error)
            }
            if(titleReport.text.length == 0){
                titleReport.setError(error)
            }
            if (descricaoReport.text.length == 0){
                descricaoReport.setError(error)
            }else{

                val imgBitMap: Bitmap = findViewById<ImageView>(R.id.imageTaken).drawable.toBitmap()
                val imageFile: File = convertBitmapToFile("file",imgBitMap)
                val imgFileRequest: RequestBody = RequestBody.create(MediaType.parse("image/"),imageFile)
                val photo_name: MultipartBody.Part = MultipartBody.Part.createFormData("photo_name",imageFile.name,imgFileRequest)

                val title : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),titleReport.text.toString())
                val user_name: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),userName)
                val lat: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),latEdit.text.toString())
                val lng: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),lngEdit.text.toString())
                val description: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),descricaoReport.text.toString())
                val type: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),spinner.selectedItem.toString())

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.addReport(title,user_name,lat,lng,description,photo_name,type)

                call.enqueue(object : Callback<ReportOutpost> {
                    override fun onResponse(call: Call<ReportOutpost>, response: Response<ReportOutpost>) {
                        if (response.isSuccessful) {
                            if (response.errorBody() != null) {
                                Toast.makeText(this@AddReport, "Erro", Toast.LENGTH_SHORT).show()
                            }else {
                                val intent = Intent(this@AddReport, MapsActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ReportOutpost>, t: Throwable) {
                        Toast.makeText(this@AddReport, "${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })

            }

        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    openCamera()
                }
                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            imageTaken.setImageURI(image_uri)
        }
    }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(this@AddReport.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

}