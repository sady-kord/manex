package com.baman.manex.ui.profile.inbox

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.databinding.FragmentInboxBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.autoCleared
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import javax.inject.Inject

class InboxFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<FragmentInboxBinding>()

    private lateinit var viewModel: InboxViewModel

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var adapter: InboxAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_inbox,
            container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(
            InboxViewModel::class.java
        )

        binding.toolbar.setTitle(resources.getString(R.string.inbox_title))
        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }
        binding.toolbar.setFongroundTintResource(R.color.black)
        binding.toolbar.setFirstIcon(R.drawable.ic_support) {
            PublicFunction.actionCall(binding.root.context, "02188985421")
        }

        adapter = InboxAdapter(appExecutors) {
            //on inbox item Click
        }

        binding.recyclerView.adapter = adapter

        skeletonScreen = Skeleton.bind(binding.recyclerView)
            .adapter(adapter)
            .load(R.layout.adapter_inbox_skeleton)
            .count(4)
            .shimmer(true)
            .show()



        Handler(Looper.getMainLooper()).postDelayed({
            skeletonScreen.hide()
//            var list = mutableListOf<InboxDto>()
//            list.add(InboxDto(0, "a"))
//            list.add(InboxDto(1, "b"))
//            list.add(InboxDto(2, "c"))
//            list.add(InboxDto(3, "d"))
//
//            adapter.submitList(list)
        }, 3000)

    }

}