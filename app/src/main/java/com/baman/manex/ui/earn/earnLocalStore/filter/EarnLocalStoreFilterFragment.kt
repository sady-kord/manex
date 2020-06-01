package com.baman.manex.ui.earn.earnLocalStore.filter

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baman.manex.AppExecutors
import com.baman.manex.R
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFilterFragment
import com.baman.manex.ui.earn.earnLocalStore.EarnLocalStoreViewModel
import com.baman.manex.util.toPersianNumber
import javax.inject.Inject

class EarnLocalStoreFilterFragment : BaseFilterFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var executors: AppExecutors

    private lateinit var viewModel: EarnLocalStoreViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(
            activity!!,
            viewModelFactory
        ).get(EarnLocalStoreViewModel::class.java)

        rangeLayoutInvisible()
        switchLayoutInvisible()
        monthLayoutInvisible()

        val value = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.clearFilter()

                findNavController().navigateUp()

                isEnabled = false
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, value)

        viewModel.getFilterCount.observe(this, Observer {
            it.handle(this, requireActivity()) { data,code->
                val stringBuilder = StringBuilder()
                stringBuilder.append("نمایش ").append(data.count.toString().toPersianNumber())
                    .append(" فروشگاه")
                getButtonSubmit().text = stringBuilder
            }
        })

        viewModel.loadFilterData.observe(this@EarnLocalStoreFilterFragment, Observer { resource ->
            resource.handle(this, requireActivity()) { data,code->
                getCheckboxList().setItems(data.filterItems)
            }
        })

        viewModel.filterRequestDto.observe(this, Observer {
            getButtonCancel().isEnabled = viewModel.isInitialFilter().not()
        })

        // If the user already has set a filter, set it on ui
        viewModel.filterRequestDto.value?.let {
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
        }

        getButtonSubmit().setOnClickListener {
            viewModel.submitFilter()
            findNavController().navigateUp()
        }

        getCheckboxList().setListLayoutManager(LinearLayoutManager(context))

    }

    override fun onTagSelected(key: String) {

    }


}