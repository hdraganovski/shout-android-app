package me.dragon.shout.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import android.content.res.Resources.NotFoundException
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.MapStyleOptions
import me.dragon.shout.LiveService
import me.dragon.shout.LiveServiceData
import me.dragon.shout.R


class MapFragment : Fragment(), OnMapReadyCallback, LiveService.Listener {
    private var mapFragment: SupportMapFragment? = null
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_map, container, false)

        if(mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
            mapFragment?.getMapAsync(this)
        }

        childFragmentManager.beginTransaction().replace(R.id.mapFragment_mapFragment, mapFragment!!).commit()

        return root
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        val latLng = LatLng(41.9990051, 21.2848064)

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = googleMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this.context, R.raw.map_style)
            ) ?: false

            if (!success) {
                Log.e("Map", "Style parsing failed.")
            }
        } catch (e: NotFoundException) {
            Log.e("Map", "Can't find style. Error: ", e)
        }


        googleMap?.addMarker(
            MarkerOptions().position(latLng)
                .title("Skopje")
        )
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            LiveService.instance.addListener(this)
        }
        else {
            LiveService.instance.removeListener(this)
        }
    }

    override fun onReceive(str: String) {
        activity?.runOnUiThread {
            context?.also {
                Toast.makeText(it, str, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onReceive(obj: LiveServiceData) {
        val location = LatLng(obj.lat, obj.lon)

        activity?.runOnUiThread {
            map?.addMarker(
                MarkerOptions().position(location)
                    .title(obj.title)
            )
        }
    }

    override fun onError(str: String) {
        activity?.runOnUiThread {
            context?.also {
                Toast.makeText(it, str, Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = MapFragment()
    }
}
