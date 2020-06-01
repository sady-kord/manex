package com.baman.manex.ui.profile.faq

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.data.model.FaqType
import com.baman.manex.databinding.FragmentFaqBinding
import com.baman.manex.databinding.FragmentInboxBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.profile.inbox.InboxAdapter
import com.baman.manex.ui.profile.inbox.InboxDto
import com.baman.manex.ui.profile.inbox.InboxViewModel
import com.baman.manex.util.autoCleared
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import javax.inject.Inject

class FaqFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<FragmentFaqBinding>()

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen

    private lateinit var viewModel: FaqViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var adapter: FaqAdapter

    val args: FaqFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_faq,
            container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(
            FaqViewModel::class.java
        )

        adapter = FaqAdapter(appExecutors) {}

        var type = args.type

        when(type){
            FaqType.Partner->{
                binding.toolbar.setTitle(resources.getString(R.string.faq_partner))
            }
            FaqType.User->{
                binding.toolbar.setTitle(resources.getString(R.string.faq_user))
            }
        }

        viewModel.setType(args.type.code)

        binding.recyclerView.adapter = adapter

        skeletonScreen = Skeleton.bind(binding.recyclerView)
            .adapter(adapter)
            .load(R.layout.adapter_faq_skeleton)
            .count(4)
            .shimmer(true)
            .show()

        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }
        binding.toolbar.setFongroundTintResource(R.color.black)
        binding.toolbar.setFirstIcon(R.drawable.ic_support) {}



        viewModel.faqData.observe(this, Observer { res ->
            res.handle(this, requireActivity(), skeletonScreen) {it,code->
                adapter.submitList(it)
            }
        })

    }

}