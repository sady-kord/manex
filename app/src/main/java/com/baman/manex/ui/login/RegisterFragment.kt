package com.baman.manex.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.R
import com.baman.manex.databinding.FragmentRegisterBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFragment
import com.baman.manex.util.AppSignatureHashHelper
import com.baman.manex.util.KeyBoardHelper
import com.baman.manex.util.autoCleared
import kotlinx.android.synthetic.main.control_text_input_layout.view.*
import javax.inject.Inject

class RegisterFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding by autoCleared<FragmentRegisterBinding>()

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_register, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // we pass the activity here to get the viewModel as shared viewModel
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(LoginViewModel::class.java)

        var keyBoard = KeyBoardHelper(requireActivity())

        val appSignatureHashHelper = AppSignatureHashHelper(requireContext())
        viewModel.setHash(appSignatureHashHelper.appSignatures[0])

        binding.phoneLayout.setNumericKeyboard()
        binding.phoneLayout.requestFocus()
        binding.phoneLayout.clear_edit_text.requestFocus()

        viewModel.validation.observe(this, Observer {
            viewModel.setPhoneValidationRule(it.cellPhone!!)
        })

        viewModel.phoneValidity.observe(this, Observer {
            binding.btnLayout.isEnabled = it
        })

        binding.phoneLayout.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.setPhoneNumber(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.textLayout.setOnClickListener {

            setEnableText(false)

            keyBoard.closeKeyBoard()

            viewModel.setPhoneForGetToken(viewModel.phoneNumber.value.toString())

            viewModel.getToken.observe(this, Observer { data ->
                data.handle(this,requireActivity(),null,
                    null,null,
                    binding.textLayout,binding.loading,{msg,code ->
                        setEnableText(true)
                    }){it,code->

                    setEnableText(true)

                    viewModel.setToken(it.token)
//                    viewModel.resendVerifyCode()

                    var bundle = Bundle()
                    bundle.putString("phone" , viewModel.phoneNumber.value.toString())
                    bundle.putString("token" , it.token)

                    findNavController().navigate(
                      R.id.verifyCodeFragment , bundle
                    )


                }
            })
        }
    }


    private fun setEnableText(enable:Boolean){
        if (enable){
            binding.phoneLayout.clear_edit_text.isEnabled = true
            binding.phoneLayout.clear_edit_text.isClickable= true
        }else{
            binding.phoneLayout.clear_edit_text.isEnabled = false
            binding.phoneLayout.clear_edit_text.isClickable= false
        }
    }
}