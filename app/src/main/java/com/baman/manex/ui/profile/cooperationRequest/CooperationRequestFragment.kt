package com.baman.manex.ui.profile.cooperationRequest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.R
import com.baman.manex.databinding.FragmentCooperationRequestBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.KeyBoardHelper
import com.baman.manex.util.autoCleared
import com.baman.manex.util.snack.SnackHelper
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import javax.inject.Inject

class CooperationRequestFragment : Fragment() , Injectable {

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private var binding by autoCleared<FragmentCooperationRequestBinding>()
    private lateinit var viewModel : CooperationRequestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_cooperation_request ,
            container , false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity() , viewModelFactory).get(
            CooperationRequestViewModel::class.java)

        binding.toolbar.setTitle(resources.getString(R.string.cooperation_request))
        toolbar.showUpIcon(true) {
            KeyBoardHelper(requireActivity()).closeKeyBoard()
            findNavController().navigateUp()
        }
        binding.toolbar.setFongroundTintResource(R.color.black)

        binding.phone.setNumericKeyboard()
        binding.name.requestFocus()

        binding.textLayout.setOnClickListener {

            binding.loading.visibility = View.VISIBLE
            binding.textLayout.visibility = View.GONE

            KeyBoardHelper(requireActivity()).closeKeyBoard()

            Handler(Looper.getMainLooper()).postDelayed({
                if (binding.name.getValue() == "" || binding.family.getValue() == "" ||
                    binding.phone.getValue() == "" || binding.shopName.getValue() == ""){

                    binding.loading.visibility = View.GONE
                    binding.textLayout.visibility = View.VISIBLE

                    SnackHelper.showSnack(requireActivity(), "فیلدهای موردنظر تکمیل نشده است")
                }else{
                    binding.loading.visibility = View.GONE
                    binding.textLayout.visibility = View.VISIBLE
                }
            },1000)
        }

    }


}