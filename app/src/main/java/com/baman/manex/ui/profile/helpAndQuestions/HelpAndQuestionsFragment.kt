package com.baman.manex.ui.profile.helpAndQuestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.data.model.FaqType
import com.baman.manex.databinding.FragmentHelpAndQuestionsBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.autoCleared
import javax.inject.Inject

class HelpAndQuestionsFragment : Fragment() , Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<FragmentHelpAndQuestionsBinding>()

    private lateinit var viewModel: HelpAndQuestionsViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_help_and_questions,
            container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity() , viewModelFactory).get(
            HelpAndQuestionsViewModel::class.java
        )

        binding.toolbar.setTitle(resources.getString(R.string.help_and_questions_title))
        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }
        binding.toolbar.setFongroundTintResource(R.color.black)

        binding.toolbar.setFirstIcon(R.drawable.ic_support){
            PublicFunction.actionCall(binding.root.context, "02188985421")
        }


        binding.faqUserRelative.setOnClickListener {
            findNavController().navigate(
                HelpAndQuestionsFragmentDirections.faq(
                    FaqType.User
                )
            )
        }
        binding.faqPartnerRelative.setOnClickListener {
            findNavController().navigate(
                HelpAndQuestionsFragmentDirections.faq(
                    FaqType.Partner
                )
            )
        }


    }

}