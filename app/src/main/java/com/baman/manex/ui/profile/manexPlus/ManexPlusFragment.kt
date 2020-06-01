package com.baman.manex.ui.profile.manexPlus

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.R
import com.baman.manex.databinding.FragmentManexPlusBinding
import com.baman.manex.databinding.FragmentManexPlusCollapsinglayoutBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.CollapsingToolbarFragment
import com.baman.manex.util.CustomTypefaceSpan
import com.baman.manex.util.autoCleared
import com.baman.manex.util.toPersianNumber
import javax.inject.Inject

class ManexPlusFragment : CollapsingToolbarFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<FragmentManexPlusBinding>()
    private var collapsingLayoutBinding by autoCleared<FragmentManexPlusCollapsinglayoutBinding>()
    private lateinit var viewModel: ManexPlusViewModel

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_manex_plus, container, false)

        return binding.root
    }

    override fun onCreateCollapsingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        collapsingLayoutBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_manex_plus_collapsinglayout, container, false)
        return collapsingLayoutBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ManexPlusViewModel::class.java)

        getToolbar().setTitle(getString(R.string.manex_plus))

        getToolbar().setFongroundTintResource(R.color.black)

        getToolbar().showUpIcon(true) { findNavController().navigateUp() }

        collapsingLayoutBinding.imageUp.setOnClickListener {
            findNavController().popBackStack()
        }

        setMockData(binding.adaptrone.earnTextSubtitle)
        setMockData(binding.adaptrtwo.earnTextSubtitle)
        setMockData(binding.adaptrthree.earnTextSubtitle)

    }

    private fun setMockData(earnTextSubtitle: AppCompatTextView) {
        val manexCount = "1".toPersianNumber()
        val text = requireContext().getString(
            R.string.onlineadapteritem_subtitle_format,
            manexCount
        )
        val ssb = SpannableStringBuilder(text)
        val startingIndex = text.indexOf(manexCount)
        val colorSpan = ForegroundColorSpan(
            ContextCompat.getColor(
                requireContext(),
                R.color.adaptertem_manexcount_textcolor
            )
        )
        ssb.setSpan(
            colorSpan, startingIndex, startingIndex + manexCount.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        val typeface = ResourcesCompat.getFont(binding.root.context, R.font.iranyekan_bold)

        ssb.setSpan(
            CustomTypefaceSpan("", typeface),
            startingIndex, startingIndex + manexCount.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        earnTextSubtitle.text = ssb
    }

}