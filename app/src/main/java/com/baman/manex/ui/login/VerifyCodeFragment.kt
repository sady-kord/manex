package com.baman.manex.ui.login

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.baman.manex.Coordinator
import com.baman.manex.R
import com.baman.manex.data.dto.TokenInputDto
import com.baman.manex.data.dto.VerifyTokenDto
import com.baman.manex.databinding.FragmentVerifyCodeBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.*
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.mukesh.OnOtpCompletionListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class VerifyCodeFragment : Fragment(), Injectable, LoginActivity.OnOtpRecieve {

    companion object {
        private const val CODE_LENGTH = 4
    }

    var binding by autoCleared<FragmentVerifyCodeBinding>()

    var smsReceiver: SMSReceiver? = null

    var TAG = VerifyCodeFragment::class.java.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val args: VerifyCodeFragmentArgs by navArgs()

    private lateinit var token: String
    private lateinit var viewModel: VerifyCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_verify_code,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(VerifyCodeViewModel::class.java)

        var keyBoard = KeyBoardHelper(requireActivity())

        val appSignatureHashHelper = AppSignatureHashHelper(requireContext())
        viewModel.setHash(appSignatureHashHelper.appSignatures[0])

        viewModel.setPhoneNumber(arguments?.getString("phone")!!)
        token = arguments?.getString("token")!!

        viewModel.setTimer()

        (activity as LoginActivity?)!!.setOnOtpRecieveListener(this)

        Handler().postDelayed({
            binding.scrollView.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            binding.scrollView.fullScroll(View.FOCUS_DOWN)
            binding.scrollView.scrollTo(0, 500)
        }, 300)

        binding.toolbar.setFongroundTintResource(R.color.black)
        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }

        binding.viewModel = viewModel

        binding.resendCodeButton.isEnabled = false

        binding.otpView.setOtpCompletionListener(object : OnOtpCompletionListener {
            override fun onOtpCompleted(otp: String?) {
                keyBoard.closeKeyBoard()

                viewModel.setVerifyInput(
                    TokenInputDto(
                        viewModel.phoneNumber.value.toString(),
                        otp!!, token
                    )
                )

                binding.codeLoading.visibility = View.VISIBLE

                viewModel.getVerifyToken.observe(this@VerifyCodeFragment, Observer { it ->
                    it.handle(this@VerifyCodeFragment, requireActivity(), null,
                        null,null,
                        null, null, {msg,code ->
                            binding.codeLoading.visibility = View.GONE
                            binding.otpView.setItemBackgroundResources(R.drawable.verify_error_back)
                        }) {it,code->
                        binding.codeLoading.visibility = View.GONE
                        binding.resendCodeButton.isEnabled = false
                        updateUi(viewModel.phoneNumber.value.toString(), it)
                    }
                })
            }

            override fun onOtpClear() {
                binding.otpView.setItemBackgroundResources(R.drawable.verify_back)
            }

        })

        binding.textLayout.setOnClickListener {

            viewModel.setTimer()

            binding.buttonLoading.visibility = View.VISIBLE
            binding.textLayout.visibility = View.GONE

            //call api for sending verify again
            viewModel.setPhoneForGetToken(viewModel.phoneNumber.value.toString())
            viewModel.getToken.observe(this, Observer { data ->
                data.handle(
                    this, requireActivity(), null,
                    null,null, binding.textLayout, binding.buttonLoading
                ) {it,code->
                    token
                }
            })

        }

        val imm =
            view?.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.otpView, 0)

        binding.editNumber.setOnClickListener {
            //            viewModel.setPhoneNumber(viewModel.phoneNumber.toString())
            findNavController().navigate(VerifyCodeFragmentDirections.register())
        }

        viewModel.timeLeft.observe(this, Observer
        {
            var stringBuilder = StringBuilder()
            stringBuilder.append(" ").append(it).append(" ثانیه")
            binding.timer.text = stringBuilder
        })

        viewModel.resendText.observe(this, Observer
        {
            binding.resendCodeButton.isEnabled = it
        })


    }

    private fun updateUi(phoneNumber: String, verifyTokenDto: VerifyTokenDto) {

        Preferences.setAccessToken(requireContext(), verifyTokenDto.accessToken)
        Preferences.setRefreshToken(requireContext(), verifyTokenDto.refreshToken)
        Preferences.setRegister(true, phoneNumber, requireContext())
        requireActivity().finish()
        startActivity(Coordinator.getNextIntent(requireActivity()))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::viewModel.isInitialized)
            viewModel.stopTimer()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }


    override fun onRecieve(otp: String) {
        binding.otpView.setText(otp)
    }

}