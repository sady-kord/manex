package com.baman.manex.ui.splash.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.baman.manex.R
import com.baman.manex.databinding.FragmentOnboardingLastSlideBinding
import com.baman.manex.util.Preferences

class OnBoardingLastSlideFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentOnboardingLastSlideBinding = DataBindingUtil.inflate(
            inflater , R.layout.fragment_onboarding_last_slide , container , false)

        binding.startLayout.setOnClickListener {
            Preferences.setOnBoardingDisplayed(true, requireContext())
            findNavController().navigate(OnBoardingLastSlideFragmentDirections.register())
        }
        return binding.root
    }
}