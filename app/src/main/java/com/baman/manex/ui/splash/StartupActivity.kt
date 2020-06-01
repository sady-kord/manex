package com.baman.manex.ui.splash

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.baman.manex.Coordinator
import com.baman.manex.R
import com.baman.manex.databinding.ActivityStartupBinding
import com.baman.manex.network.repositories.State
import com.baman.manex.ui.baseClass.BaseActivity
import com.baman.manex.util.snack.SnackHelper
import com.baman.manex.util.snack.SnackLength
import timber.log.Timber
import javax.inject.Inject


class StartupActivity : BaseActivity() {

    companion object {
        const val MIN_DYNAMIC_CONTENT_SHOW_LENGTH = 3000L
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var isSynced = false
    private var isDelayPassed = false

    fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideStatusBar()

        Handler(Looper.getMainLooper()).postDelayed({
            isDelayPassed = true
            checkReadyToGo()
        }, MIN_DYNAMIC_CONTENT_SHOW_LENGTH)

        val binding: ActivityStartupBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_startup)

        animateContent(binding.root)

        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)

        binding.viewModel = viewModel

        if (hasNetConnection()) {
            viewModel.syncStatus.observe(this, Observer {
                when (it.status) {
                    State.SYNCING -> {
                        Timber.i(it.message)
                    }
                    State.ERROR -> {
                        Timber.i(it.message)
                        SnackHelper.snack(
                            this,
                            getString(R.string.startup_no_internet),
                            SnackLength.LENGTH_INDEFINITE
                        )
                    }
                    State.SUCCESS -> {
                        Timber.i("Sync finish")
                        isSynced = true
                        checkReadyToGo()
                    }
                }
            })

        }else{
            var height = 60f
            SnackHelper.showSnack(this, "عدم اتصال به اینترنت", height)
        }

    }

    private fun animateContent(view: View) {
        view.alpha = 0f
        view.animate()
            .alpha(1f)
            .setDuration(resources.getInteger(R.integer.default_animation_duration).toLong())
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun checkReadyToGo() {
        if (isDelayPassed && isSynced) {
            var intent = Coordinator.getNextIntent(this)
            startActivity(intent)
            finish()
        }
    }

}
