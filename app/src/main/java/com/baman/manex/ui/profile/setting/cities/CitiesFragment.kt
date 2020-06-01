package com.baman.manex.ui.profile.setting.cities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.databinding.FragmentCitiesBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFragment
import com.baman.manex.util.snack.SnackHelper
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import kotlinx.android.synthetic.main.toolbar.view.*
import javax.inject.Inject

class CitiesFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: FragmentCitiesBinding

    private lateinit var viewModel: CitiesViewModel

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen


    private var TAG = CitiesFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_cities, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =
            ViewModelProvider(activity!!, viewModelFactory).get(CitiesViewModel::class.java)

        binding.toolbar.setFongroundTintResource(R.color.black)

        binding.toolbar.image_up.setOnClickListener { findNavController().navigateUp() }

        binding.toolbar.text_title.text = getString(R.string.choose_city)

        val adapter = CitiesAdapter {
            Log.d(TAG, it.title + " " + it.id)
        }

        binding.recyclerView.adapter = adapter

        skeletonScreen = Skeleton.bind(binding.recyclerView)
            .adapter(adapter)
            .load(R.layout.adapter_city_skeleton)
            .count(4)
            .shimmer(true)
            .show()

        viewModel.getCities().observe(this, Observer { res ->
            res.handle(CitiesFragment(),activity,skeletonScreen) {it,code->
                adapter.setData(it)
            }
        })

    }


}