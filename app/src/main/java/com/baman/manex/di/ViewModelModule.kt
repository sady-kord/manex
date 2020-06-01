package com.baman.manex.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.github.di.ViewModelKey
import com.baman.manex.ui.profile.about.AboutManexViewModel
import com.baman.manex.ui.profile.addCard.AddCardViewModel
import com.baman.manex.ui.profile.cooperationRequest.CooperationRequestViewModel
import com.baman.manex.ui.earn.earnLocalStore.EarnLocalStoreViewModel
import com.baman.manex.ui.earn.earnLocalStore.detail.EarnLocalStoreDetailViewModel
import com.baman.manex.ui.earn.earnLocalStore.detail.nearBranch.NearBranchListViewModel
import com.baman.manex.ui.earn.earnLocalStore.detail.OtherBranches.OtherBranchesViewModel
import com.baman.manex.ui.earn.earnLocalStore.map.MapViewModel
import com.baman.manex.ui.earn.earnLocalStore.search.EarnLocalStoreSearchViewModel
import com.baman.manex.ui.home.exoVideoPlayer.VideoPlayerViewModel
import com.baman.manex.ui.profile.faq.FaqViewModel
import com.baman.manex.ui.profile.helpAndQuestions.HelpAndQuestionsViewModel
import com.baman.manex.ui.profile.inbox.InboxViewModel
import com.baman.manex.ui.profile.inviteFriend.InviteFriendViewModel
import com.baman.manex.ui.login.LoginViewModel
import com.baman.manex.ui.login.VerifyCodeViewModel
import com.baman.manex.ui.main.MainActivityViewModel
import com.baman.manex.ui.burn.BurnFragmentViewModel
import com.baman.manex.ui.burn.burnManexStore.BurnManexStoreViewModel
import com.baman.manex.ui.burn.burnManexStore.detail.BurnManexStoreDetailViewModel
import com.baman.manex.ui.burn.burnManexStore.search.BurnManexStoreSearchViewModel
import com.baman.manex.ui.burn.burnVoucher.BurnVoucherViewModel
import com.baman.manex.ui.burn.burnVoucher.detail.BurnVoucherDetailViewModel
import com.baman.manex.ui.burn.burnVoucher.purchase.PurchaseVoucherDetailViewModel
import com.baman.manex.ui.burn.burnVoucher.search.BurnVoucherSearchViewModel
import com.baman.manex.ui.earn.EarnViewModel
import com.baman.manex.ui.earn.earnVoucher.EarnVoucherViewModel
import com.baman.manex.ui.earn.earnVoucher.detail.EarnVoucherDetailViewModel
import com.baman.manex.ui.earn.earnVoucher.search.EarnVoucherSearchFragment
import com.baman.manex.ui.earn.earnVoucher.search.EarnVoucherSearchViewModel
import com.baman.manex.ui.home.HomeViewModel
import com.baman.manex.ui.home.search.HomeSearchViewModel
import com.baman.manex.ui.profile.ProfileViewModel
import com.baman.manex.ui.profile.manexPlus.ManexPlusViewModel
import com.baman.manex.ui.profile.myCards.MyCardsViewModel
import com.baman.manex.ui.profile.myTransaction.MyTransactionViewModel
import com.baman.manex.ui.profile.myTransaction.search.MyTransactionSearchViewModel
import com.baman.manex.ui.splash.onboarding.OnBoardingViewModel
import com.baman.manex.ui.onlineService.OnlineServiceViewModel
import com.baman.manex.ui.onlineService.detail.OnlineServiceDetailViewModel
import com.baman.manex.ui.profile.myShopping.MyShoppingViewModel
import com.baman.manex.ui.profile.setting.cities.CitiesViewModel
import com.baman.manex.ui.profile.setting.SettingViewModel
import com.baman.manex.ui.splash.SplashViewModel
import com.baman.manex.ui.profile.terms.TermsAndConditionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModelJava: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnBoardingViewModel::class)
    abstract fun bindOnBoardingViewModel(onBoardingViewModel: OnBoardingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EarnViewModel::class)
    abstract fun bindEarnViewModel(earnViewModel: EarnViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BurnFragmentViewModel::class)
    abstract fun bindBurnViewModel(burnViewModel: BurnFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineServiceViewModel::class)
    abstract fun bindEarnOnlineViewModel(earnOnlineViewModel: OnlineServiceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BurnVoucherViewModel::class)
    abstract fun bindBurnVoucherViewModel(burnVoucherViewModel: BurnVoucherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BurnManexStoreViewModel::class)
    abstract fun bindBurnStoreManexViewModel(burnStoreManexViewModel: BurnManexStoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EarnLocalStoreViewModel::class)
    abstract fun bindEarnLocalStoreViewModel(earnLocalStoreViewModel: EarnLocalStoreViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(EarnLocalStoreSearchViewModel::class)
    abstract fun bindSearchViewModel(offlineStoreSearchViewModel: EarnLocalStoreSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BurnVoucherSearchViewModel::class)
    abstract fun bindGiftCardViewModel(burnVoucherSearchViewModel: BurnVoucherSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BurnManexStoreSearchViewModel::class)
    abstract fun bindShopSearchViewModel(burnManexStoreSearchViewModel: BurnManexStoreSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(editProfileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EarnLocalStoreDetailViewModel::class)
    abstract fun bindEarnLocalStoreDetailViewModel(earnLocalStoreDetailViewModel: EarnLocalStoreDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineServiceDetailViewModel::class)
    abstract fun bindOnlineServiceDetailViewModel(onlineServiceDetailViewModel: OnlineServiceDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BurnManexStoreDetailViewModel::class)
    abstract fun bindBurnStoreManexDetailViewModel(burnStoreManexDetailViewModel: BurnManexStoreDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BurnVoucherDetailViewModel::class)
    abstract fun bindVoucherDetailViewModel(voucherDetailViewModel: BurnVoucherDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyTransactionViewModel::class)
    abstract fun bindMyTransactionViewModel(myTransactionViewModel: MyTransactionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyTransactionSearchViewModel::class)
    abstract fun bindTransactionSearchViewModel(transactionSearchViewModel: MyTransactionSearchViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(CitiesViewModel::class)
    abstract fun bindTransactionCitiesViewModel(citiesViewModel: CitiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    abstract fun bindSettingViewModel(settingViewModel: SettingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PurchaseVoucherDetailViewModel::class)
    abstract fun bindPurchaseVoucherDetailViewModel(purchaseVoucherDetailViewModel: PurchaseVoucherDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddCardViewModel::class)
    abstract fun bindAddCardViewModel(addCardViewModel: AddCardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyCardsViewModel::class)
    abstract fun bindMyCardsViewModel(myCardsViewModel: MyCardsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InviteFriendViewModel::class)
    abstract fun bindInviteFriendViewModel(inviteFriendViewModel: InviteFriendViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NearBranchListViewModel::class)
    abstract fun bindNearBranchListViewModel(nearBranchListViewModel: NearBranchListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OtherBranchesViewModel::class)
    abstract fun bindOtherBranchesViewModel(otherBranchesViewModel: OtherBranchesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AboutManexViewModel::class)
    abstract fun bindAboutManexViewModel(aboutManexViewModel: AboutManexViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TermsAndConditionViewModel::class)
    abstract fun bindTermsAndConditionViewModel(termsAndConditionViewModel: TermsAndConditionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CooperationRequestViewModel::class)
    abstract fun bindCooperationRequestViewModel(cooperationRequestViewModel: CooperationRequestViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InboxViewModel::class)
    abstract fun bindInboxViewModel(inboxViewModel: InboxViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FaqViewModel::class)
    abstract fun bindFaqViewModel(faqViewModel: FaqViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HelpAndQuestionsViewModel::class)
    abstract fun bindHelpAndQuestionsViewModel(helpAndQuestionsViewModel: HelpAndQuestionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(mapViewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VerifyCodeViewModel::class)
    abstract fun bindVerifyCodeViewModel(verifyCodeViewModel: VerifyCodeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideoPlayerViewModel::class)
    abstract fun bindVideoPlayerViewModel(videoPlayerViewModel: VideoPlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManexPlusViewModel::class)
    abstract fun bindManexPlusViewModel(manexPlusViewModel: ManexPlusViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeSearchViewModel::class)
    abstract fun bindHomeSearchViewModel(homeSearchViewModel: HomeSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EarnVoucherDetailViewModel::class)
    abstract fun bindEarnVoucherDetailViewModel(homeSearchViewModel: EarnVoucherDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EarnVoucherSearchViewModel::class)
    abstract fun bindEarnVoucherSearchViewModel(homeSearchViewModel: EarnVoucherSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EarnVoucherViewModel::class)
    abstract fun bindEarnVoucherViewModel(homeSearchViewModel: EarnVoucherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyShoppingViewModel::class)
    abstract fun bindMyShoppingViewModel(myShoppingViewModel: MyShoppingViewModel): ViewModel
}