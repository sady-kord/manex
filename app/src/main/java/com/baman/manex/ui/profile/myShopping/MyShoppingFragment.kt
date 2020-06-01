package com.baman.manex.ui.profile.myShopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.R
import com.baman.manex.databinding.FragmentMyShoppingBinding
import com.baman.manex.util.autoCleared
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_my_shopping.*
import javax.inject.Inject

class MyShoppingFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding by autoCleared<FragmentMyShoppingBinding>()

    private lateinit var tabsPagerAdapter: TabsPagerAdapter

    private var isResumedFragment = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_shopping, container, false)

        isResumedFragment = true
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val elevation = resources.getDimensionPixelSize(R.dimen.tabs_elevation).toFloat()
        if (tabs != null)
            ViewCompat.setElevation(tabs, elevation)

        tabsPagerAdapter = TabsPagerAdapter(
            context!!,
            childFragmentManager!!
        )

        view_pager.adapter = tabsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        toolbar.setTitle(resources.getString(R.string.my_shopping_list))
        toolbar.showUpIcon(true) { findNavController().navigateUp() }
        toolbar.setFongroundTintResource(R.color.black)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



    }
}


