package com.baman.manex.ui.earn.earnLocalStore.detail.OtherBranches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.databinding.FragmentOtherBranchesBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.autoCleared
import javax.inject.Inject

class OtherBranchesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private var binding by autoCleared<FragmentOtherBranchesBinding>()
    private lateinit var viewModel: OtherBranchesViewModel
    private lateinit var adapter : OtherBranchesAdapter

    val args: OtherBranchesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_other_branches,
            container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(OtherBranchesViewModel::class.java)

        binding.toolbar.setTitle(resources.getString(R.string.other_branch) + " " + args.otherBranch.shortName)
        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }
        binding.toolbar.setFongroundTintResource(R.color.black)

        adapter =
            OtherBranchesAdapter(
                appExecutors
            )
        adapter.submitList(args.otherBranch.otherBranchs)

        binding.recyclerView.adapter = adapter


    }

}