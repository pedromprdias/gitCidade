package ipvc.estg.cidadeinteligente

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cidadeinteligente.api.EndPoints
import ipvc.estg.cidadeinteligente.api.ReportOutpost
import ipvc.estg.cidadeinteligente.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.info_window.view.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var mMap: GoogleMap

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallBack: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private var clicked = false

    private lateinit var reports: List<ReportOutpost>

    val markerID: HashMap<Marker, Int> = HashMap()
    lateinit var marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE)

        var idd = sharedPreferences.getString("userPref","defaultName")

        idUser.setText(idd)

        val intent = Intent(this, AddReport::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallBack = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                val lngInt = lastLocation.longitude
                val latInt = lastLocation.latitude
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))

                addReport.setOnClickListener {
                    intent.putExtra("lng",lngInt.toString())
                    intent.putExtra("lat",latInt.toString())
                    startActivity(intent)
                }

            }
        }

        createLocationRequest()

        val menuFab = findViewById<FloatingActionButton>(R.id.menuMap)

        menuFab.setOnClickListener {
            onAddButtonClicked()
        }

        idLogout.setOnClickListener {
            var sharedPreferences: SharedPreferences = getSharedPreferences("pref",Context.MODE_PRIVATE)
                with(sharedPreferences.edit()){
                    putString("userPref",null)
                    putBoolean("autoLogin",false)
                    commit()
                }
            val intent = Intent(this, MenuInicio::class.java)
            startActivity(intent)
        }

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()
        var position: LatLng

        call.enqueue(object : Callback<List<ReportOutpost>> {
            override fun onResponse(call: Call<List<ReportOutpost>>, response: Response<List<ReportOutpost>>) {
                if (response.isSuccessful) {
                    reports = response.body()!!
                    for (report in reports) {
                        position = LatLng(report.lat, report.lng)
                        marker = mMap.addMarker(MarkerOptions().position(position).title(report.title))

                        markerID.put(marker, report.id)
                    }
                }
            }

            override fun onFailure(call: Call<List<ReportOutpost>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked:Boolean) {
        if (!clicked){
            addReport.visibility = View.VISIBLE
            idLogout.visibility = View.VISIBLE

        }else{
            addReport.visibility = View.INVISIBLE
            idLogout.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnInfoWindowClickListener{
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getreportById(markerID[it]!!)

            call.enqueue(object : Callback<ReportOutpost> {
                override fun onResponse(call: Call<ReportOutpost>, response: Response<ReportOutpost>) {
                    if(response.isSuccessful) {
                        val point = response.body()!!

                        val edi:String=getString(R.string.editR)
                        val del:String=getString(R.string.deleteR)
                        val info:String=getString(R.string.infoR)

                        val alertDialogBuilder = AlertDialog.Builder(this@MapsActivity)

                        alertDialogBuilder.setNeutralButton(edi) { dialog, which ->
                            val intent = Intent(this@MapsActivity, InfoMarker::class.java)
                            intent.putExtra("id", markerID[it]!!)
                            intent.putExtra("titleInt", point.title)
                            intent.putExtra("titleInt", point.description)
                            startActivity(intent)
                        }

                        alertDialogBuilder.setNegativeButton(del){ dialog, which ->
                            deletePoint(point.id)
                            Toast.makeText(applicationContext, info, Toast.LENGTH_SHORT).show()
                            mMap.clear()
                            getPoints()
                        }

                        alertDialogBuilder.show()
                    }
                }

                override fun onFailure(call: Call<ReportOutpost>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
        //setUpMap()
    }

    fun setUpMap(){
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION) , LOCATION_PERMISSION_REQUEST_CODE)

            return
        }else{
            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this) {location ->

                if(location!=null){
                    lastLocation=location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }
    }

    private fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallBack,null)
    }

    private fun createLocationRequest(){
        locationRequest = LocationRequest()

        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallBack)
    }

    public override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    class CustomInfoWindowGoogleMap(val context: Context) : GoogleMap.InfoWindowAdapter {

        override fun getInfoContents(p0: Marker?): View {

            var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.info_window, null)
            var mInfoWindow: ReportOutpost? = p0?.tag as ReportOutpost?

            mInfoView.titleInfo.text = mInfoWindow?.title
            mInfoView.infoDesc.text = mInfoWindow?.description

            return mInfoView
        }

        override fun getInfoWindow(p0: Marker?): View? {
            return null
        }
    }

    fun deletePoint(id: Int){
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.deleteReport(id)

        call.enqueue(object: Callback<ReportOutpost>{
            override fun onFailure(call: Call<ReportOutpost>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ReportOutpost>, response: Response<ReportOutpost>) {
                getPoints()
            }
        })

    }

    fun getPoints(){

        val request = ServiceBuilder.buildService(EndPoints::class.java)

        val call = request.getReports()

        var position: LatLng

        call.enqueue(object: Callback<List<ReportOutpost>>{
            override fun onResponse(call: Call<List<ReportOutpost>>, response: Response<List<ReportOutpost>>) {
                if (response.isSuccessful) {
                    reports = response.body()!!
                    for (report in reports) {
                        position = LatLng(report.lat, report.lng)
                        marker = mMap.addMarker(MarkerOptions().position(position).title(report.title))

                        markerID.put(marker, report.id)
                    }
                    }
                }

            override fun onFailure(call: Call<List<ReportOutpost>>, t: Throwable) {
                //Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
