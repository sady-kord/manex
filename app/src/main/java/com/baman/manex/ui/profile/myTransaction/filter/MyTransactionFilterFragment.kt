package com.baman.manex.ui.profile.myTransaction.filter

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFilterFragment
import com.baman.manex.ui.profile.myTransaction.MyTransactionViewModel
import com.baman.manex.util.toPersianNumber
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import kotlinx.android.synthetic.main.view_manex_range_filter.view.*
import javax.inject.Inject

class MyTransactionFilterFragment : BaseFilterFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var executors: AppExecutors

    private lateinit var viewModel: MyTransactionViewModel

    private lateinit var groupTitles: List<String>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(activity!!, viewModelFactory)
            .get(MyTransactionViewModel::class.java)

        getTransactionFilterOption()

        getFilterSwitchView().visibility = View.GONE

        getFilterCount()

        getCheckboxListTitle().text = resources.getString(R.string.earn_manex_status)

        getCheckboxList().setup(executors) {
            viewModel.setFilterTag(it)
        }

        viewModel.filterRequestDto.observe(this, Observer {
            getButtonCancel().isEnabled = viewModel.isInitialFilter().not()
        })

        // If the user already has set a filter, set it on ui
        viewModel.filterRequestDto.value?.let {

            if (it.filterModel?.minValue != 0 && it.filterModel?.maxValue != null) {
                getFilterManexRange().setManexRangeValue(
                    it.filterModel?.minValue ?: 0,
                    it.filterModel?.maxValue ?: 0
                )
            }

            getCheckboxList().setSelectedItems(it.filterModel?.filterTags!!)

            var setChips = mutableSetOf<String>()
            for (i in 0 until it.filterModel?.groupTitle?.size!!) {
                it.filterModel?.groupTitle!![i].let { res ->
                    setChips.add(res)
                }
            }
            getChipgroup().setup(childFragmentManager, setChips, -1)

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.clearFilter()

                    findNavController().navigateUp()

                    isEnabled = false
                }
            })

        getButtonCancel().setOnClickListener {
            viewModel.resetFilter()
            getCheckboxList().setSelectedItems(emptyList())
            getChipgroup().clearSelectedItems()
            getFilterSwitchView().setSwitchState(false)
            getFilterManexRange().setManexRangeValue(
                viewModel.filterRequestDto.value?.filterModel?.minValue ?: 0,
                viewModel.filterRequestDto.value?.filterModel?.maxValue ?: 0
            )
        }

        getButtonSubmit().setOnClickListener {
            viewModel.submitFilter()
            findNavController().navigateUp()
        }

        getFilterManexRange().setOnTrackingChangeListener(object :
            OnRangeChangedListener {
            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                getFilterManexRange().manex_count_start.text =
                    leftValue.toInt().toString().toPersianNumber()

                getFilterManexRange().manex_count_end.text =
                    rightValue.toInt().toString().toPersianNumber()

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                if (isLeft) {
                    viewModel.setFilterMinValue(view!!.leftSeekBar.progress.toInt())
                } else {
                    viewModel.setFilterMaxValue(view!!.rightSeekBar.progress.toInt())
                }
            }
        })

        getCheckboxList().setListLayoutManager(LinearLayoutManager(context))

    }

    private fun getFilterCount() {
//        viewModel.refresh()
        viewModel.getFilterCount.observe(this, Observer {
            it.handle(this, requireActivity()) { data,code->
                val stringBuilder = StringBuilder()
                stringBuilder.append("نمایش ").append(data.count.toString().toPersianNumber())
                    .append(" تراکنش")
                getButtonSubmit().text = stringBuilder
            }
        })
    }

    private fun getTransactionFilterOption() {

        viewModel.getTransactionFilterOption.observe(this, Observer {
            it.handle(this, requireActivity()) { it,code->

                viewModel.setFilterMinValue(it.minManexCount)
                viewModel.setFilterMaxValue(it.maxManexCount)

                if (it.minManexCount != it.maxManexCount) {
                    getFilterManexRange().setLimits(it.minManexCount, it.maxManexCount)
                } else {
                    getFilterManexRange().visibility = View.GONE
                    getSwitchDivider().visibility = View.GONE
                }

                getCheckboxList().setItems(it.types)

                var setChips = mutableSetOf<String>()
                for (i in 0 until it.groupTitles.size) {
                    if (it.groupTitles[i] != null)
                        it.groupTitles[i].let { setChips.add(it) }
                }
                getChipgroup().setup(childFragmentManager, setChips, -1)
                setTags(it.groupTitles,-1)

                groupTitles = it.groupTitles

            }
        })
    }

    override fun onTagSelected(key: String) {

        viewModel.setChipsTag(key)
    }

}