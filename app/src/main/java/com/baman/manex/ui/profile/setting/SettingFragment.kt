package com.baman.manex.ui.profile.setting

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrognito.flashbar.Flashbar
import com.baman.manex.App
import com.baman.manex.R
import com.baman.manex.databinding.FragmentSettingBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.DeviceStatus
import com.baman.manex.util.Preferences
import com.baman.manex.util.autoCleared
import com.baman.manex.util.snack.SnackHelper
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.exitProcess

class SettingFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var app:App

    private var binding by autoCleared<FragmentSettingBinding>()
    private lateinit var viewModel: SettingViewModel
    private var flashbar: Flashbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_setting,
            container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(
            SettingViewModel::class.java
        )

        binding.toolbar.setTitle(resources.getString(R.string.setting))
        binding.toolbar.showUpIcon(true) { findNavController().navigateUp() }
        binding.toolbar.setFongroundTintResource(R.color.black)

        binding.exitLayout.setOnClickListener {
            showExitDialog()
        }

        binding.cityLayout.setOnClickListener {
            findNavController().navigate(SettingFragmentDirections.cities())
        }
    }


    private fun showExitDialog() {

        var height = 86f
        if (DeviceStatus.hasSoftKeys(requireActivity().windowManager, requireContext()))
            height = DeviceStatus.getNavBarHeight(requireContext()).toFloat()

        val dialogBuilder = AlertDialog.Builder(context!!)
        dialogBuilder.setMessage(resources.getString(R.string.exit_manex_question))
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, id ->

                viewModel.logout.observe(this, Observer {
                    it.handle(this,requireActivity()){it,code->
                        if(it.value){
                            app.restartAndLogoutApplication(app.baseContext)

                            Preferences.setRegister(false, "" , context!!)

                            activity?.moveTaskToBack(true)
                            activity?.finish()
                            exitProcess(-1)
                        }else{
                            flashbar = SnackHelper.showSnack(
                                requireActivity(),
                                "خطایی رخ داده ٬ مجدد تلاش کنید",
                                height
                            )
                        }
                    }
                })

            }
            .setNegativeButton(resources.getString(R.string.no)) { dialog, id ->
                dialog.cancel()
            }


        val alert = dialogBuilder.create()
        alert.show()

        val view: Window? = alert.window
        val message: TextView = view!!.findViewById(android.R.id.message)
        val confirmButton: Button = view.findViewById(android.R.id.button1)
        val cancelButton: Button = view.findViewById(android.R.id.button2)

        var customFont = ResourcesCompat.getFont(context!!, R.font.iranyekan)

        message.typeface = customFont

        message.setTextColor(ContextCompat.getColor(context!!, R.color.colorTextPrimary))
        confirmButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        cancelButton.setTextColor(ContextCompat.getColor(context!!, R.color.setting_dialog_cancel))
    }
}