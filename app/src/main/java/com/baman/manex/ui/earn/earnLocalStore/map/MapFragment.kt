package com.baman.manex.ui.earn.earnLocalStore.map

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.R
import com.baman.manex.databinding.FragmentMapBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseActivity
import com.baman.manex.ui.earn.earnLocalStore.EarnLocalStoreViewModel
import com.baman.manex.util.autoCleared
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.master.permissionhelper.PermissionHelper
import kotlinx.android.synthetic.main.list_feature.view.*
import javax.inject.Inject


class MapFragment : Fragment() , Injectable {

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private var binding by autoCleared<FragmentMapBinding>()

    private lateinit var viewModel : EarnLocalStoreViewModel

    private lateinit var mMapView : MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_map ,
            container , false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(
            EarnLocalStoreViewModel::class.java)

        binding.toolbar.setTitle(resources.getString(R.string.earn_local_store_title))
        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }
        binding.toolbar.setFongroundTintResource(R.color.black)

        mMapView = binding.mapView

        binding.feature.search.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.search())
        }

        binding.feature.filter.setOnClickListener {

            findNavController().navigate(MapFragmentDirections.filter())
        }


        binding.fab.setOnClickListener {
            findNavController().navigateUp()
        }


        viewModel.requestDto.observe(this, Observer {
            binding.feature.setFilterSelected(!viewModel.isInitialFilter())
            binding.feature.setSortSelected(it.sortItem != viewModel.defaultSortData)
        })

        mMapView.onCreate(savedInstanceState)

        getLocationPermission()

    }


    private fun getLocationPermission() {
        val permissionHelper = (context as BaseActivity)
            .getPermissionHelper(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 100
            )

        permissionHelper.request(object : PermissionHelper.PermissionCallback {
            override fun onPermissionGranted() {
                initMap()

            }

            override fun onIndividualPermissionGranted(strings: Array<String>) {
                initMap()
            }

            override fun onPermissionDenied() {

            }

            override fun onPermissionDeniedBySystem() {
            }
        })
    }

    fun initMap() {

        mMapView.onResume()
        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView.getMapAsync { mMap ->
            googleMap = mMap
            googleMap.isMyLocationEnabled = true

            val sydney = LatLng((-34).toDouble(), 151.0)
            googleMap.addMarker(MarkerOptions().position(sydney).title("Title").snippet("Marker Description"))

            val cameraPosition =
                CameraPosition.Builder().target(sydney).zoom(12f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }


    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
        viewModel.resetFilter()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }

}