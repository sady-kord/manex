package com.baman.manex.ui.burn.burnManexStore.filter

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFilterFragment
import com.baman.manex.ui.burn.burnManexStore.BurnManexStoreViewModel
import com.baman.manex.util.toPersianNumber
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import kotlinx.android.synthetic.main.view_manex_range_filter.view.*
import javax.inject.Inject

class BurnManexStoreFilterFragment : BaseFilterFragment(), Injectable {

    override fun onTagSelected(key: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var executors: AppExecutors

    private lateinit var viewModel: BurnManexStoreViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(
            activity!!,
            viewModelFactory
        ).get(BurnManexStoreViewModel::class.java)

        viewModel.getFilterCount.observe(this, Observer {
            it.handle(this, requireActivity()) { data,code->
                val stringBuilder = StringBuilder()
                stringBuilder.append("نمایش ").append(data.value.toString().toPersianNumber())
                    .append(" کالا")
                getButtonSubmit().text = stringBuilder
            }
        })

        monthLayoutInvisible()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.clearFilter()

                    findNavController().navigateUp()

                    isEnabled = false
                }
            })

        viewModel.loadFilterData.observe(this@BurnManexStoreFilterFragment, Observer { resource ->
            resource.handle(this, requireActivity()) { data,code->
                getFilterManexRange().setLimits(data.minPrice, data.maxPrice)
                getCheckboxList().setItems(data.filterItems)
            }
        })

        viewModel.filterRequestDto.observe(this, Observer {
            getButtonCancel().isEnabled = viewModel.isInitialFilter().not()
        })

        // If the user already has set a filter, set it on ui
        viewModel.filterRequestDto.value?.let {
            if (it.minValue != 0 && it.maxValue != null) {
                getFilterManexRange().setManexRangeValue(it.minValue ?: 0, it.maxValue ?: 0)
            }
            getFilterSwitchView().setSwitchState(it.canBuy!!)
            if (!it.tags.isNullOrEmpty())
                getCheckboxList().setSelectedItems(it.tags!!)
        }

        getCheckboxListTitle().text = resources.getString(R.string.shops_category)
        getCheckboxList().setup(executors) {
            viewModel.setFilterTags(it)
        }

        getButtonCancel().setOnClickListener {
            viewModel.resetFilter()
            getCheckboxList().setSelectedItems(emptyList())
            getFilterSwitchView().setSwitchState(false)
            getFilterManexRange().setManexRangeValue(
                viewModel.filterRequestDto.value?.minValue ?: 0,
                viewModel.filterRequestDto.value?.maxValue ?: 0
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
                getFilterManexRange().manex_count_start.text = leftValue.toInt().toString()
                    .toPersianNumber()

                getFilterManexRange().manex_count_end.text = rightValue.toInt().toString()
                    .toPersianNumber()

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                if (isLeft) {
                    viewModel.setFilterMinValue(view!!.leftSeekBar.progress.toInt())
                } else {
                    viewModel.setFilterMaxValue(view!!.rightSeekBar.progress.toInt())
                }
            }
        })


        getFilterSwitchView().setOnSwitchClickListener(CompoundButton.OnCheckedChangeListener
        { _, isChecked ->
            viewModel.setFilterCanBy(isChecked)
        })

        getCheckboxList().setListLayoutManager(GridLayoutManager(context, 1))

    }

}