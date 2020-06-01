package com.baman.manex.controls.buttonlistsheet

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.baman.manex.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_purchase_bottom_sheet.*


class PurchaseBottomSheetDialog : BottomSheetDialogFragment() {

     private val KEY_TITLE = "title"
     private val KEY_SUB_TITLE = "subTitle"

    private lateinit var title: String
    private lateinit var subTitle: String

    private lateinit var bottomSheetTitle : TextView
    private lateinit var bottomSheetSubTitle : TextView
    private lateinit var cancelButton : AppCompatTextView
    private lateinit var confirmButton : AppCompatTextView

    private var callBack: BottomSheetCallBack? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate( R.layout.layout_purchase_bottom_sheet,container,false)

        bottomSheetTitle = view.findViewById(R.id.bottom_sheet_title)
        bottomSheetSubTitle = view.findViewById(R.id.bottom_sheet_sub_title)

        cancelButton = view.findViewById(R.id.bottom_sheet_cancel_button)
        confirmButton = view.findViewById(R.id.bottom_sheet_confirm_button)

        cancelButton.setOnClickListener {
            dismiss()
        }

        confirmButton.setOnClickListener {
            if (callBack != null) {
                callBack?.confirmCallBack()
                dismiss()
            }
        }

        if (arguments != null) {
            title = arguments!!.getString("title")!!
            subTitle = arguments!!.getString("subTitle")!!
        }

        setItem(title , subTitle)

        return view
    }


    fun setItem(_title: String, _subTitle: String) {
        bottomSheetTitle.text = _title
        bottomSheetSubTitle.text = _subTitle
    }

    fun setCallBack(callBack: BottomSheetCallBack) {
        this.callBack = callBack
    }

    interface BottomSheetCallBack {
        fun confirmCallBack()
    }

    companion object {
        @JvmStatic
        fun newInstance(title : String , subTitle: String): PurchaseBottomSheetDialog {
            val frag = PurchaseBottomSheetDialog()
            val args = Bundle()
            args.putString("title", title)
            args.putString("subTitle", subTitle)
            frag.arguments = args
            return frag
        }
    }


}

