package com.baman.manex.ui.splash.onboarding

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.baman.manex.R
import com.baman.manex.data.dto.OnBoardingSlideDto
import com.baman.manex.databinding.FragmentOnboardingBinding
import com.baman.manex.di.Injectable
import com.baman.manex.util.Preferences
import com.baman.manex.util.autoCleared
import javax.inject.Inject

class OnBoardingFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: OnBoardingViewModel by viewModels { viewModelFactory }

    var binding by autoCleared<FragmentOnboardingBinding>()

    private var slides = mutableListOf<OnBoardingSlideDto>()

    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
            override fun onPageSelected(position: Int) {
                setActiveDot(getPageSize() - 1 - position)

                when {
                    position == 0 -> {
                        //last page
                        binding.layoutDots.visibility = View.GONE
                    }
                    position < getPageSize() - 1 -> {
                        binding.layoutDots.visibility = View.VISIBLE
                    }
                    position == getPageSize() - 1 -> {
                        //first page
                        binding.layoutDots.visibility = View.VISIBLE
                    }
                }
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (Preferences.getOnBoardingDisplayed(context)) {
            findNavController().navigate(OnBoardingFragmentDirections.register())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_onboarding, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        setData()
        setupSlides()
    }

    fun setData(){
        var item1 = OnBoardingSlideDto("1", R.drawable.ic_onboarding1,
            "دریافت منکس از هر خرید" ,
            "شما در منکس برای تمام خریدها و تراکنش های خود منکس دریافت می کنید")

        var item2 = OnBoardingSlideDto("1", R.drawable.ic_onboarding2 ,
            "سود بیشتر در تکرار خرید" ,
            "از خریدهای خود منکس کسب نموده و از آن در خریدهای دیگر خود استفاده کنید")

        var item3 = OnBoardingSlideDto("1", R.drawable.ic_onboarding3 ,
            "استفاده از منکس به جای پول" ,
            "استفاده از منکس بسیار ساده است ، شما با منکس تجربه متفاوت و جذابی را لمس می کنید و می توانید از منکس در خریدهای بعدی استفاده کنید")
        //slides = it.data.reversed()
        slides.add(item3)
        slides.add(item2)
        slides.add(item1)
    }

    private fun goToNextStep() {
        Preferences.setOnBoardingDisplayed(true, requireContext())
        findNavController().navigate(OnBoardingFragmentDirections.register())
    }

    private fun getPageSize() = slides.size + 1

    private fun setupSlides() {
        setActiveDot(0)

        val viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(childFragmentManager)
        viewPager.currentItem = getPageSize()

        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

    }

    private fun setActiveDot(currentPage: Int) {
        val dots = arrayOfNulls<TextView>(getPageSize())
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        binding.layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(requireContext())
            dots[i]?.setText(Html.fromHtml("&#8226;"))
            dots[i]?.setTextSize(35f)
            dots[i]?.setTextColor(colorsInactive[currentPage])
            binding.layoutDots.addView(dots[i])
        }

        if (dots.isNotEmpty())
            dots[currentPage]?.setTextColor(colorsActive[currentPage])
    }

    internal inner class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {

            return when (position) {
                0 -> OnBoardingLastSlideFragment()
                else -> OnBoardingSlideFragment.instantiate(
                    slides[position - 1]
                )
            }
//            return OnBoardingSlideFragment.instantiate(slides[position])
        }

        override fun getCount() = slides.size + 1
    }
}
