package com.baman.manex.ui.baseClass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.baman.manex.R
import com.baman.manex.controls.CheckBoxList
import com.baman.manex.controls.ChoiceChipGroup
import com.baman.manex.controls.FilterManexRangeView
import com.baman.manex.controls.FilterSwitchView
import com.baman.manex.network.service.SearchTag
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_basefilter.*
import kotlinx.android.synthetic.main.fragment_basesearch.*

abstract class BaseFilterFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_basefilter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        filter_image_close.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    protected fun setTags(tags: List<String>,indexSelected :Int) {
        val set = mutableSetOf<String>()
        for (i in 0 until tags.size) {
            if (tags[i] != null)
                tags[i].let { set.add(it) }
        }

        filter_chipgroup.setOnTagSelectedListener {
            onTagSelected(it)
        }
        filter_chipgroup.setup(childFragmentManager, set,indexSelected )
    }

    open fun onTagSelected(key: String) {}

    protected fun getFilterManexRange(): FilterManexRangeView = filter_manex_range
    protected fun getSwitchDivider() = switch_divider
    protected fun getFilterSwitchView(): FilterSwitchView = filter_switch_view
    protected fun getCheckboxListTitle(): TextView = text_checkboxlist
    protected fun getCheckboxList(): CheckBoxList = checkbox_list
    protected fun getButtonSubmit(): TextView = button_submit
    protected fun getButtonCancel(): TextView = button_cancel
    protected fun getChipgroup(): ChoiceChipGroup = filter_chipgroup
    protected fun getMonthLayout(): LinearLayout = month_layout

    fun monthLayoutInvisible() {
        month_layout.visibility = View.GONE
        month_divider.visibility = View.GONE
    }

    fun checkBoxListInvisible() {
        checkbox_list.visibility = View.GONE
        check_divider.visibility = View.GONE
    }

    fun switchLayoutInvisible() {
        filter_switch_view.visibility = View.GONE
        switch_divider.visibility = View.GONE
    }

    fun rangeLayoutInvisible() {
        filter_manex_range.visibility = View.GONE
    }

}