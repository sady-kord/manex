package com.baman.manex.ui.earn.earnLocalStore.detail.nearBranch

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
import com.baman.manex.databinding.FragmentMyCardsBinding
import com.baman.manex.databinding.FragmentNearBranchListBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.autoCleared
import javax.inject.Inject

class NearBranchListFragment : Fragment(),Injectable {

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var adapter : NearBranchAdapter

    private var binding by autoCleared<FragmentNearBranchListBinding>()

    private lateinit var viewModle : NearBranchListViewModel

    val args: NearBranchListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_near_branch_list ,
            container , false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModle = ViewModelProvider(requireActivity(), viewModelFactory).get(
            NearBranchListViewModel::class.java)

        adapter =
            NearBranchAdapter(
                appExecutors
            ) {
                //on near store Click
            }

        binding.toolbar.setTitle(resources.getString(R.string.nearbybranch_title))
        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }
        binding.toolbar.setFongroundTintResource(R.color.black)

        adapter.submitList(args.storeInfo.nearBranchs)

        binding.recyclerView.adapter = adapter

    }

}