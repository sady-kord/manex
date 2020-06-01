package com.baman.manex.ui.splash.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.baman.manex.R
import com.baman.manex.data.dto.OnBoardingSlideDto
import com.baman.manex.databinding.FragmentOnboardingslideBinding
import com.baman.manex.util.Preferences

class OnBoardingSlideFragment : Fragment() {

    companion object {
        const val KEY_SLIDE = "slide"

        fun instantiate(slide: OnBoardingSlideDto): OnBoardingSlideFragment {
            val args = Bundle()
            args.putParcelable(KEY_SLIDE, slide)
            val fragment =
                OnBoardingSlideFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var slide: OnBoardingSlideDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        slide = arguments?.getParcelable(KEY_SLIDE)
            ?: throw IllegalStateException(
                "Slide arguments is missing, Use provided instantiate" +
                        " function for creation."
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOnboardingslideBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_onboardingslide, container, false)

        binding.slide = slide
        binding.imageView.setImageDrawable(
            VectorDrawableCompat.create(
                resources,
                slide!!.imagePath,
                null
            )
        )
        binding.title.text = slide!!.title
        binding.description.text = slide!!.subtitle

        binding.cancelLayout.setOnClickListener {
            Preferences.setOnBoardingDisplayed(true, requireContext())
            findNavController().navigate(OnBoardingSlideFragmentDirections.register())
        }

        return binding.root
    }

}
