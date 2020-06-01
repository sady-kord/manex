package com.baman.manex.ui.common

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window.FEATURE_NO_TITLE
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.baman.manex.R
import com.baman.manex.controls.RadioButtonControl
import com.baman.manex.data.dto.SortDto
import java.util.*
import javax.inject.Inject


class SortDialogFragment : DialogFragment() {

    private var callBack: DialogCallBack? = null

    lateinit var radioGroup: RadioGroup

    lateinit var close: AppCompatImageView

    private lateinit var dataList: ArrayList<SortDto>

    var defaultSelectedItem: Int = -1

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_fragment_sort, null)


        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(FEATURE_NO_TITLE)
        }


        builder.setView(view)

        val alertDialog = builder.create()

        radioGroup = view.findViewById(R.id.radio_group)

        close = view.findViewById(R.id.close_img)

        close.setOnClickListener {
            dismiss()
        }

        if (arguments != null) {
            dataList = arguments!!.getParcelableArrayList<SortDto>("data") as ArrayList<SortDto>
            defaultSelectedItem = arguments!!.getInt("check")
        }

        setSortList(dataList)

        setDefaultSelect()

        radioGroup.setOnCheckedChangeListener { p0, checkedId ->
            val selectedId = radioGroup.checkedRadioButtonId
            var radioButton: RadioButtonControl = view.findViewById(selectedId)
            setBackGroundOnRadio(radioButton)
            if (callBack != null) {

                for (i in 0 until dataList.size) {
                    if (radioButton.text == dataList[i].title) {
                        callBack!!.onCallBack(dataList[i].key)
                        dismiss()
                    }
                }
            }
        }

        return alertDialog
    }

    private fun setDefaultSelect() {

        var default = ""
        dataList.forEach { res ->
            if (res.key == defaultSelectedItem) {
                default = res.title
            }
        }

        for (i in 0 until radioGroup.size) {
            var radio = radioGroup[i] as RadioButtonControl

            if (radio.text.equals(default)) {
                radioGroup.check(radio.id)
                setBackGroundOnRadio(radio)
            }

        }
    }

    private fun setSortList(list: ArrayList<SortDto>) {

        for (s in list) {
            val radioButton = RadioButtonControl(activity, null)
            radioButton.text = s.title
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
        fun onCallBack(key: Int)
    }

    companion object {
        @JvmStatic
        fun newInstance(data: List<SortDto>?, checkedItem: Int): SortDialogFragment {
            val frag = SortDialogFragment()
            val args = Bundle()
            var dataList: ArrayList<SortDto> = ArrayList()
            for (item in data!!) {
                dataList.add(item)
            }
            args.putParcelableArrayList("data", dataList)
            args.putInt("check", checkedItem)
            frag.arguments = args
            return frag
        }
    }


}
