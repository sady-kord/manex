package com.baman.manex.ui.profile.about

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
import com.baman.manex.databinding.FragmentAboutUsBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.autoCleared
import com.bumptech.glide.Glide
import javax.inject.Inject

class AboutManexFragment : Fragment() , Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<FragmentAboutUsBinding>()
    private lateinit var viewModel: AboutManexViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_about_us,
            container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity() , viewModelFactory).get(
            AboutManexViewModel::class.java
        )

        viewModel.getAbout.observe(this, Observer {res ->
            res.handle(this,requireActivity()){it,code->
                Glide.with(requireContext()).load(it.fileUrl).into(binding.logo)
                binding.title.text = it.title
                binding.description.text = it.description
            }
        })

        binding.toolbar.setTitle(resources.getString(R.string.about_manex))
        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }
        binding.toolbar.setFongroundTintResource(R.color.black)
        binding.toolbar.setFirstIcon(R.drawable.ic_home){}
        binding.toolbar.getFirstIcon().setOnClickListener {
            findNavController().navigate(AboutManexFragmentDirections.home())
        }
        binding.termsButton.setOnClickListener {
           findNavController().navigate(AboutManexFragmentDirections.terms())
        }
    }

}