package com.example.pokemon_game2

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkpermission()
        loadpokemon()
    }
    var ACCESSLOCATION=123
    fun checkpermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this , android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION) , ACCESSLOCATION)
                return
            }
        }
        getuserlocation()
    }
    fun getuserlocation(){
        Toast.makeText(this , "Location access on" , Toast.LENGTH_LONG).show()
        var mylocation = MyLocationListener()
        var locationManager= getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 3, 3f , mylocation)
        var mythread= mythread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            ACCESSLOCATION->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getuserlocation()
                }
                else{
                    Toast.makeText(this , "Access denied" , Toast.LENGTH_LONG).show()

                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

        // Add a marker in Sydney and move the camera
       }
    var location:Location?=null

    inner class MyLocationListener:LocationListener{

        constructor(){
            location=Location("start")
            location!!.longitude=0.0
            location!!.latitude=0.0

        }
        override fun onLocationChanged(p0: Location?) {
            //this.location=p0
            location=p0
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("Not yet implemented")
        }
        override fun onProviderEnabled(provider: String?) {
            TODO("Not yet implemented")
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("Not yet implemented")
        }
    }
    var oldlocation:Location?=null
    inner class mythread:Thread{
        constructor():super(){
            oldlocation=Location("start")
            oldlocation!!.longitude=0.0
            oldlocation!!.latitude=0.0

        }
        override  fun run(){
            while(true){
                try {
                    if(oldlocation!!.distanceTo(location)==0f){
                        continue
                    }
                    oldlocation=location
                    runOnUiThread(){
                        mMap!!.clear()
                        //show player
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions().position(sydney).title("me")
                            .snippet("ASH:" )
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.person2)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney , 14f)) // 1-24f 24 being max zoom
                        // show pokemon
                        for(i in 0..listpokemons.size-1){
                            var newpok = listpokemons[i]
                            if(newpok.iscatch==false){
                               // Toast.makeText(this , "${newpok.lat} , ${newpok.log}" ,Toast.LENGTH_LONG ).show()
                                //Toast.makeText(this , "Access denied" , Toast.LENGTH_LONG).show()

                                val pokemonloc = LatLng(newpok.lat!!, newpok.log!! )
                                mMap.addMarker(MarkerOptions().position(pokemonloc).title(newpok.name!!)
                                    .snippet(newpok.des)
                                    .icon(BitmapDescriptorFactory.fromResource(newpok.image!!)))
                               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney , 1f)) // 1-24f 24 being max zoom

                                // var newpokloc:Location?=null
                               // newpokloc!!.latitude=newpok.lat!!
                               // newpokloc!!.longitude=newpok.log!!
                                if(location!!.distanceTo(newpok.location)<2){
                                    newpok.iscatch=true
                                    listpokemons[i]= newpok
                                    playerpower+= newpok.power!!
                                    Toast.makeText(applicationContext , " ${newpok.name} Captured !! Power increased to:"+ listpokemons[i].power , Toast.LENGTH_LONG).show()

                                }
                            }
                        }


                    }
                    Thread.sleep(1000)

                }catch (ex:Exception){

                }
            }
        }
    }
    var playerpower=0.0
    var listpokemons= ArrayList<pokemon>()
        fun loadpokemon(){
            listpokemons.add(pokemon(R.drawable.p11 , "CHARMANDER" , "here is from japan" , 55.0 , 77.20289 , 28.686598))
            listpokemons.add(pokemon(R.drawable.p22 , "Boobasauras" , "here is from Gaziabad" , 90.0 , 77.24028,28.6917  ))
            listpokemons.add(pokemon(R.drawable.p33 , "Squirtle" , "here is from USA" , 80.0 , 77.1898,28.6784 ))
        }

}