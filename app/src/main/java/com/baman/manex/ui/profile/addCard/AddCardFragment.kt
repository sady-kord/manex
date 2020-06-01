package com.baman.manex.ui.profile.addCard

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.baman.manex.R
import com.baman.manex.databinding.FragmentAddCardBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.CustomTypefaceSpan
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.autoCleared
import kotlinx.android.synthetic.main.layout_ticket_mid.view.*
import javax.inject.Inject

class AddCardFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<FragmentAddCardBinding>()

    private lateinit var viewModel: AddCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_add_card, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(AddCardViewModel::class.java)

        binding.toolbar.setTitle(getString(R.string.add_card_with_transaction))

        binding.toolbar.setFongroundTintResource(R.color.black)

        binding.toolbar.showUpIcon(true) {
            findNavController().navigateUp()
        }


        showIntro(true)

        binding.toolbar.setFirstIcon(R.drawable.ic_add_card) {
            showIntro(true)
        }

        setUptickets()

        binding.addCardBuyManex.setOnClickListener {
            viewModel.refresh()
            viewModel.getToken.observe(this, Observer {
                it.handle(AddCardFragment(), activity) { data,code->
                    PublicFunction.openBrowserDialog(
                        requireActivity(),
                        "https://pay.manexapp.com/pay/pay/payment?tokenId=${data.guid}"
                    )
                }
            })
        }

        handleClicks()

    }

    private fun setUptickets() {
        binding.ticketTap30.image_ticket.setImageDrawable(
            VectorDrawableCompat.create(resources ,
                R.drawable.ic_add_card_tap30,
                null)
        )
        binding.ticketTap30.ticketView.backgroundColor = resources.getColor(R.color.tap30_ticket)
        binding.ticketTap30.title_ticket.text = "کارت هدیه تپسی"
        val typeface = ResourcesCompat.getFont(context!!, R.font.iranyekan_regular)
        val amountTap30 = "۱۲ هزار تومان"
        val text = getString(R.string.tap30_amount , amountTap30)
        val ssb = SpannableStringBuilder(text)
        val startingIndex = text.indexOf(amountTap30)
        ssb.setSpan(
            CustomTypefaceSpan("", typeface),
            startingIndex, startingIndex + amountTap30.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        binding.ticketTap30.sub_title_ticket.text = ssb
        binding.ticketTap30.pay_ticket.text = "پرداخت ۲ هزار تومان"

        binding.ticketIranTick.image_ticket.setImageDrawable(
            VectorDrawableCompat.create(resources ,
                R.drawable.ic_add_card_iran_ticket,
                null))
        binding.ticketIranTick.ticketView.backgroundColor = resources.getColor(R.color.iran_tick_ticket)
        binding.ticketIranTick.title_ticket.text = "کارت هدیه ایران تیک"
        val amountTick = "۱۵ هزار تومان"
        val textTick = getString(R.string.tick_amount , amountTick)
        val ssbTick = SpannableStringBuilder(textTick)
        val startingIndexTick = textTick.indexOf(amountTick)
        ssbTick.setSpan(
            CustomTypefaceSpan("", typeface),
            startingIndexTick, startingIndexTick + amountTick.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        binding.ticketIranTick.sub_title_ticket.text = ssbTick
        binding.ticketIranTick.pay_ticket.text = "پرداخت ۵ هزار تومان"

    }

    fun handleClicks() {
        binding.confirmButton.setOnClickListener {
            // binding.intro.visibility = View.GONE
            showIntro(false)
        }

        binding.buyChargeCard.setOnClickListener {
            PublicFunction.openBrowserDialog(
                requireActivity(),
                "https://webapp.724.ir"
            )
        }

        binding.payBillCard.setOnClickListener {
            PublicFunction.openBrowserDialog(
                requireActivity(),
                "https://webapp.724.ir"
            )
        }

        binding.balanceCard.setOnClickListener {
            PublicFunction.openBrowserDialog(
                requireActivity(),
                "https://webapp.724.ir"
            )
        }

        binding.buyInternetCard.setOnClickListener {
            PublicFunction.openBrowserDialog(
                requireActivity(),
                "https://webapp.724.ir"
            )
        }

        binding.intro.setOnClickListener {
            //do nothing
        }
    }

    private fun showIntro(show: Boolean) {
        if (show) {
            binding.intro.visibility = View.VISIBLE
            binding.intro.animate().alpha(1.0f).duration = 200
        } else {
            binding.intro.animate().alpha(0.0f).duration = 200
            binding.intro.visibility = View.GONE
        }
    }
}