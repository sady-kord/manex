package com.baman.manex.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.baman.manex.R
import com.baman.manex.testing.SingleFragmentActivity
import com.baman.manex.ui.login.LoginViewModel
import com.baman.manex.ui.login.RegisterFragment
import com.baman.manex.util.*
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class RegisterFragmentTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<SingleFragmentActivity> =
        ActivityTestRule(SingleFragmentActivity::class.java)

    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityRule)
    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()
    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()

    private val registerFragment = RegisterFragment()

    private val repoLiveData = MutableLiveData<Boolean>()

    @Before
    fun init(){
        val viewModel = mock(LoginViewModel::class.java)
        `when`(viewModel.phoneValidity).thenReturn(repoLiveData)
        registerFragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
        activityRule.activity.setFragment(registerFragment)

        EspressoTestUtil.disableProgressBarAnimations(activityRule)
    }

    @Test
    fun testMobilePhone(){
        onView(withId(R.id.edit_layout)).perform(click())
        assertTrue(isKeyboardShown())
    }

    private fun isKeyboardShown(): Boolean {
        val inputMethodManager = InstrumentationRegistry.getInstrumentation()
            .targetContext.getSystemService(
            Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isAcceptingText
    }

}