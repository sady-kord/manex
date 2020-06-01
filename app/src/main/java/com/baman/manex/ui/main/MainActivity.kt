package com.baman.manex.ui.main

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.baman.manex.R
import com.baman.manex.ui.baseClass.BaseActivity
import com.baman.manex.util.PublicFunction
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)


        val res: Resources = resources
        val config =
            Configuration(res.configuration)
        Log.d("BaseActivity", " " + config.layoutDirection)


        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_navhost) as NavHostFragment? ?: return

        val navController = host.navController
        navbar?.setupWithNavController(navController)

        val navBarElevation =
            resources.getDimensionPixelSize(R.dimen.main_navbar_elevation).toFloat()
        if (navbar != null)
            ViewCompat.setElevation(navbar, navBarElevation)

        val menu: Menu = navbar.menu

        navController.addOnDestinationChangedListener { _: NavController, destination, _: Bundle? ->
            val id = destination.id
            val isMainPageDestination =
                id == R.id.destination_burn
                        || id == R.id.earnFragment
//                        || id == R.id.destination_main
                        || id == R.id.destination_profile

            setIconOnNavBar(menu,id)

            navbar.visibility = if (isMainPageDestination) View.VISIBLE else View.GONE
        }


        if (intent.hasExtra("path")) {
            if (intent.getStringExtra("path") == getString(R.string.deep_linking_path_add_card)) {
                navController.navigate(R.id.destination_my_cards)
            }
            if (intent.getStringExtra("path") == getString(R.string.deep_linking_path_buy_voucher)) {
                navController.navigate(R.id.destination_my_shopping)
            }
        }

    }

    private fun setIconOnNavBar(menu: Menu, selectedId: Int) {

//        menu.findItem(R.id.destination_main).icon =PublicFunction.getDrawable(R.drawable.ic_home_24_px, this)
        menu.findItem(R.id.earnFragment).icon =PublicFunction.getDrawable(R.drawable.ic_earn_24_px, this)
        menu.findItem(R.id.destination_burn ).icon =PublicFunction.getDrawable(R.drawable.ic_redeem_24_px, this)
        menu.findItem(R.id.destination_profile ).icon =PublicFunction.getDrawable(R.drawable.ic_profile_24_px, this)

        when (selectedId) {
            R.id.destination_main -> {
                menu.findItem(selectedId).icon =PublicFunction.getDrawable(R.drawable.ic_home_24_px_active, this)
            }
            R.id.earnFragment -> {
                menu.findItem(selectedId).icon =PublicFunction.getDrawable(R.drawable.ic_earn_24_px_active, this)
            }
            R.id.destination_burn -> {
                menu.findItem(selectedId).icon =PublicFunction.getDrawable(R.drawable.ic_redeem_24_px_active, this)
            }
        }

    }
}

