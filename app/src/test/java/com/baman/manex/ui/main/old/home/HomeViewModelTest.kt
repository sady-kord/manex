package com.baman.manex.ui.main.old.home

import com.baman.manex.network.repositories.UserRepository
import com.baman.manex.ui.home.HomeViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
//import org.robolectric.RobolectricTestRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @Mock
    lateinit var drawerRepo: DrawerRepository
//
    @Mock
    lateinit var userRepo: UserRepository
//
    lateinit var userViewModel: HomeViewModel

    @Before
    fun onSetup() {
        MockitoAnnotations.initMocks(this);
        userViewModel =
            HomeViewModel(userRepo, drawerRepo)
    }

    //    @Rule
//    @JvmField
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    private val drawerRepo = Mockito.mock(DrawerRepository::class.java)
//    private val userRepo = Mockito.mock(UserRepository::class.java)
//    private val userViewModel = HomeViewModel(userRepo, drawerRepo)
//
    @Test
    fun checkUserInfo() {
//        userViewModel.getUserInfo.observeForever(mock())
        verify(userRepo).getUserInfo()

    }
}