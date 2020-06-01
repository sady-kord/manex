package com.baman.manex.ui.profile.inviteFriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.databinding.FragmentInviteFriendBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.toPersianNumber
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import javax.inject.Inject

class InviteFriendFragment : Fragment(), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: FragmentInviteFriendBinding

    private lateinit var viewModel: InviteFriendViewModel

    private var shareUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_invite_friend, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =
            ViewModelProvider(activity!!, viewModelFactory).get(InviteFriendViewModel::class.java)


        viewModel.getData.observe(this, Observer {res ->
            res.handle(this,requireActivity()) {it,code->
                binding.title.text = it.title.toPersianNumber()
                binding.subTitle.text = it.description.toPersianNumber()
                binding.code.text = it.code.toPersianNumber()
                binding.earnManexText.text = it.earnedManex.toString().toPersianNumber()
                binding.verifiedInviteCount.text = it.verifiedInviteesCount.toString().toPersianNumber()
                binding.invitedCount.text = it.inviteesCount.toString().toPersianNumber()
                shareUrl = it.sharedUrl
            }
        })

        binding.imageUp.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.inviteText.setOnClickListener {
            PublicFunction.shareLinkDialog(requireContext(), shareUrl)
        }


    }

}