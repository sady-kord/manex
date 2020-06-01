package com.baman.manex

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.multidex.MultiDex
import com.baman.manex.db.ManexDb
import com.baman.manex.di.AppInjector
import com.baman.manex.ui.splash.StartupActivity
import com.baman.manex.util.LocaleHelper
import com.baman.manex.util.Preferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.exitProcess


open class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var manexDb: ManexDb

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun activityInjector() = activityInjector

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)

        setupTimber()

        setupFirebase()

    }

    private fun setupFirebase() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {
                override fun onComplete(task: Task<InstanceIdResult>) {
                    if (!task.isSuccessful) {
                        Timber.w(task.exception, "getInstanceId failed")
                        return
                    }
                    // Get new Instance ID token
                    val token = task.result!!.token
                }
            })
    }

    open fun restartAndLogoutApplication(context: Context) {
        val registerActivity = Intent(context, StartupActivity::class.java)
        registerActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(registerActivity)

        Preferences.setOnBoardingDisplayed(false, context)
        Preferences.setRegister(false, "", context)
        Preferences.setRefreshToken(context, "")
        Preferences.setAccessToken(context, "")

        appExecutors.mainThread().execute {
            manexDb.clearAllTables()
        }

        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(0)
    }


    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun attachBaseContext(base: Context) {
        MultiDex.install(this)
        LocaleHelper.setLocal(base, LocaleHelper.getLanguageSetting(this))
        super.attachBaseContext(base)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.setLocal(applicationContext, LocaleHelper.getLanguageSetting(this))
    }

}