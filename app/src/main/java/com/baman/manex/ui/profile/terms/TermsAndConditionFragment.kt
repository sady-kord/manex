package com.baman.manex.ui.profile.terms

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.R
import com.baman.manex.databinding.FragmentTermsAndConditionBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.autoCleared
import kotlinx.android.synthetic.main.fragment_terms_and_condition.*
import javax.inject.Inject


class TermsAndConditionFragment : Fragment() , Injectable {

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private var binding by autoCleared<FragmentTermsAndConditionBinding>()
    private lateinit var viewModel: TermsAndConditionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_terms_and_condition,
            container, false
        )
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity() , viewModelFactory).get(
            TermsAndConditionViewModel::class.java
        )

        binding.toolbar.setTitle(resources.getString(R.string.terms_and_condition))
        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }
        binding.toolbar.setFongroundTintResource(R.color.black)

        viewModel.getRule.observe(this, Observer {res ->
            res.handle(this,requireActivity()){it,code->
                binding.rules.setTextList(it.list)
            }
        })

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.loadUrl("https://baman.club/enamad")

    }
}