package com.baman.manex.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.baman.manex.util.CountingAppExecutors
import com.baman.manex.util.mock
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val userViewModel = LoginViewModel(CountingAppExecutors().appExecutors)

    @Test
    fun checkInvalidPhone() {
        userViewModel.phoneValidity.observeForever(mock())

        userViewModel.setPhoneNumber("0477dd23545")
        assertFalse(userViewModel.phoneValidity.value!!)

        userViewModel.setPhoneNumber("09322774623545")
        assertFalse(userViewModel.phoneValidity.value!!)
    }

    @Test
    fun checkValidPhone() {
        userViewModel.phoneValidity.observeForever(mock())

        userViewModel.setPhoneNumber("09774423545")
        assertTrue(userViewModel.phoneValidity.value!!)
    }

//    @Test
//    fun checkUserVerification() {
//        userViewModel.verifyUser.observeForever(mock())
//
//        userViewModel.setVerificationCode("1233")
//        assertTrue(userViewModel.verifyUser.value is Loading)
//    }
//
//    @Test
//    fun checkRegisterUser() {
//        val liveData = userViewModel.registerUser()
//        liveData.observeForever(mock())
//        assertTrue(liveData.value is Loading)
//    }
}