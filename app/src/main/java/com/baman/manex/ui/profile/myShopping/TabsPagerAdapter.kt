package com.baman.manex.ui.profile.myShopping

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.baman.manex.R
import com.baman.manex.ui.home.HomeFragment
import com.baman.manex.ui.profile.myShopping.manexStore.MyManexStoreFragment
import com.baman.manex.ui.profile.myShopping.voucher.MyVoucherFragment


class TabsPagerAdapter(context: Context, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    @StringRes
    private val TAB_TITLES =
        intArrayOf(R.string.gift_card, R.string.manex_store)

    private var mContext = context

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> MyVoucherFragment()
            1 -> MyManexStoreFragment()
            else -> HomeFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
        return POSITION_NONE
    }

}