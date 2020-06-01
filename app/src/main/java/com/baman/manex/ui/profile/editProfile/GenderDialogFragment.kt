package com.baman.manex.ui.profile.editProfile

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.baman.manex.R
import com.baman.manex.controls.RadioButtonControl
import com.baman.manex.data.model.GenderType
import javax.inject.Inject

class GenderDialogFragment : DialogFragment() {

    private var defaultSelectedItem: Int = 0

    private var callBack: DialogCallBack? = null

    lateinit var radioGroup: RadioGroup

    lateinit var close: AppCompatImageView

    private var data = mutableListOf<Int>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_fragment_gender, null)

        data = mutableListOf<Int>()
        data.add(1)
        data.add(2)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        builder.setView(view)

        val alertDialog = builder.create()

        radioGroup = view.findViewById(R.id.radio_group)

        close = view.findViewById(R.id.close_img)

        close.setOnClickListener {
            dismiss()
        }

        if (arguments != null) {
            defaultSelectedItem = arguments!!.getInt("check")
        }

        setSortList()

        setDefaultSelect()

        radioGroup.setOnCheckedChangeListener { p0, checkedId ->
            val selectedId = radioGroup.checkedRadioButtonId
            var radioButton: RadioButtonControl = view.findViewById(selectedId)
            setBackGroundOnRadio(radioButton)
            if (callBack != null) {
                for (i in 1 until data.size +1) {
                    if (radioButton.text == GenderType.Parse(i).value) {
                        callBack!!.onCallBack(GenderType.Parse(i).value)
                        dismiss()
                    }
                }
            }
        }

        return alertDialog
    }

    private fun setDefaultSelect() {
        for (i in 0 until radioGroup.size) {
            var radio = radioGroup[i] as RadioButtonControl
            if (radio.text.equals(GenderType.Parse(defaultSelectedItem).value)) {
                radioGroup.check(radio.id)
                setBackGroundOnRadio(radio)
            }
        }
    }

    private fun setSortList() {
        for (s in data) {
            val radioButton = RadioButtonControl(activity, null)
            radioButton.text = GenderType.Parse(s).value
            radioGroup.addView(radioButton)
        }
    }

    private fun setBackGroundOnRadio(radioButton: RadioButtonControl) {
        for (i in 0 until radioGroup.size) {
            var radio = radioGroup[i] as RadioButtonControl
            radio.clearBackGround()
        }
        radioButton.setBackGround()
    }

    fun setCallBack(callBack: DialogCallBack) {
        this.callBack = callBack
    }

    interface DialogCallBack {
        fun onCallBack(key: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(checkedItem: Int): GenderDialogFragment {
            val frag =
                GenderDialogFragment()
            val args = Bundle()
            args.putInt("check", checkedItem)
            frag.arguments = args
            return frag
        }
    }

}
