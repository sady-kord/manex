package com.baman.manex.di

import com.baman.manex.ui.profile.about.AboutManexFragment
import com.baman.manex.ui.profile.addCard.AddCardFragment
import com.baman.manex.ui.profile.cooperationRequest.CooperationRequestFragment
import com.baman.manex.ui.earn.earnLocalStore.EarnLocalStoreFragment
import com.baman.manex.ui.earn.earnLocalStore.detail.EarnLocalStoreDetailFragment
import com.baman.manex.ui.earn.earnLocalStore.detail.nearBranch.NearBranchListFragment
import com.baman.manex.ui.earn.earnLocalStore.detail.OtherBranches.OtherBranchesFragment
import com.baman.manex.ui.earn.earnLocalStore.filter.EarnLocalStoreFilterFragment
import com.baman.manex.ui.earn.earnLocalStore.map.MapFragment
import com.baman.manex.ui.earn.earnLocalStore.search.EarnLocalStoreSearchFragment
import com.baman.manex.ui.profile.editProfile.EditProfileFragment
import com.baman.manex.ui.profile.faq.FaqFragment
import com.baman.manex.ui.profile.helpAndQuestions.HelpAndQuestionsFragment
import com.baman.manex.ui.profile.inbox.InboxFragment
import com.baman.manex.ui.profile.inviteFriend.InviteFriendFragment
import com.baman.manex.ui.burn.BurnFragment
import com.baman.manex.ui.burn.burnManexStore.BurnManexStoreFragment
import com.baman.manex.ui.burn.burnManexStore.detail.BurnManexStoreDetailFragment
import com.baman.manex.ui.burn.burnManexStore.filter.BurnManexStoreFilterFragment
import com.baman.manex.ui.burn.burnManexStore.search.BurnManexStoreSearchFragment
import com.baman.manex.ui.burn.burnVoucher.BurnVoucherFragment
import com.baman.manex.ui.burn.burnVoucher.detail.BurnVoucherDetailFragment
import com.baman.manex.ui.burn.burnVoucher.filter.BurnVoucherFilterFragment
import com.baman.manex.ui.burn.burnVoucher.purchase.PurchaseVoucherDetailFragment
import com.baman.manex.ui.burn.burnVoucher.search.BurnVoucherSearchFragment
import com.baman.manex.ui.earn.EarnFragment
import com.baman.manex.ui.earn.earnVoucher.EarnVoucherFragment
import com.baman.manex.ui.earn.earnVoucher.detail.EarnVoucherDetailFragment
import com.baman.manex.ui.earn.earnVoucher.filter.EarnVoucherFilterFragment
import com.baman.manex.ui.earn.earnVoucher.search.EarnVoucherSearchFragment
import com.baman.manex.ui.home.HomeFragment
import com.baman.manex.ui.home.search.HomeSearchFragment
import com.baman.manex.ui.profile.ProfileFragment
import com.baman.manex.ui.profile.manexPlus.ManexPlusFragment
import com.baman.manex.ui.profile.myCards.MyCardsFragment
import com.baman.manex.ui.profile.myShopping.MyShoppingFragment
import com.baman.manex.ui.profile.myShopping.manexStore.MyManexStoreFragment
import com.baman.manex.ui.profile.myShopping.voucher.MyVoucherFragment
import com.baman.manex.ui.profile.myTransaction.MyTransactionFragment
import com.baman.manex.ui.profile.myTransaction.search.MyTransactionSearchFragment
import com.baman.manex.ui.profile.myTransaction.filter.MyTransactionFilterFragment
import com.baman.manex.ui.onlineService.OnlineServiceFragment
import com.baman.manex.ui.onlineService.detail.OnlineServiceDetailFragment
import com.baman.manex.ui.profile.setting.cities.CitiesFragment
import com.baman.manex.ui.profile.setting.SettingFragment
import com.baman.manex.ui.profile.terms.TermsAndConditionFragment
import com.baman.manex.ui.profile.waitingManex.WaitingManexFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeEarnFragment(): EarnFragment

    @ContributesAndroidInjector
    abstract fun contributeBurnFragment(): BurnFragment

    @ContributesAndroidInjector
    abstract fun contributeOnlineServiceFragment(): OnlineServiceFragment

    @ContributesAndroidInjector
    abstract fun contributeEarnLocalStoreFragment(): EarnLocalStoreFragment

    @ContributesAndroidInjector
    abstract fun contributeBurnStoreManexFragment(): BurnManexStoreFragment

    @ContributesAndroidInjector
    abstract fun contributeGiftCardSearchFragmentt(): BurnVoucherSearchFragment

    @ContributesAndroidInjector
    abstract fun contributeShopSearchFragment(): BurnManexStoreSearchFragment

    @ContributesAndroidInjector
    abstract fun contributeOfflineStoreSearchFragment(): EarnLocalStoreSearchFragment

    @ContributesAndroidInjector
    abstract fun contributeBurnVoucherFragment(): BurnVoucherFragment

    @ContributesAndroidInjector
    abstract fun contributeFilterRedeemGiftCardFragment(): BurnVoucherFilterFragment

    @ContributesAndroidInjector
    abstract fun contributeFilterEarnOfflineFragment(): EarnLocalStoreFilterFragment

    @ContributesAndroidInjector
    abstract fun contributeFilterRedeemShopFragment(): BurnManexStoreFilterFragment

    @ContributesAndroidInjector
    abstract fun contributeeditProfileFragmet(): EditProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeEarnLocalStoreDetailFragment(): EarnLocalStoreDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeVoucherDetailFragment(): BurnVoucherDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeOnlineServiceDetailFragment(): OnlineServiceDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeBurnStoreManexDetailFragment(): BurnManexStoreDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeEmptyFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeMyTransactionFragment(): MyTransactionFragment

    @ContributesAndroidInjector
    abstract fun contributeMyTransactionFilterFragment(): MyTransactionFilterFragment

    @ContributesAndroidInjector
    abstract fun contributeTransactionSearchFragment(): MyTransactionSearchFragment

    @ContributesAndroidInjector
    abstract fun contributeMyShoppingFragment(): MyShoppingFragment

    @ContributesAndroidInjector
    abstract fun contributeMyVoucherFragment(): MyVoucherFragment

    @ContributesAndroidInjector
    abstract fun contributeMyManexStoreFragment(): MyManexStoreFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingFragment(): SettingFragment

    @ContributesAndroidInjector
    abstract fun contributeCitiesFragment(): CitiesFragment

    @ContributesAndroidInjector
    abstract fun contributePurchaseVoucherDetailFragment(): PurchaseVoucherDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeAddCardFragment(): AddCardFragment

    @ContributesAndroidInjector
    abstract fun contributeMyCardsFragment(): MyCardsFragment

    @ContributesAndroidInjector
    abstract fun contributeInviteFriendFragment(): InviteFriendFragment

    @ContributesAndroidInjector
    abstract fun contributeNearBranchListFragment(): NearBranchListFragment

    @ContributesAndroidInjector
    abstract fun contributeOtherBranchesFragment(): OtherBranchesFragment

    @ContributesAndroidInjector
    abstract fun contributeAboutManexFragment(): AboutManexFragment

    @ContributesAndroidInjector
    abstract fun contributeTermsAndConditionFragment(): TermsAndConditionFragment

    @ContributesAndroidInjector
    abstract fun contributCooperationRequestFragment(): CooperationRequestFragment

    @ContributesAndroidInjector
    abstract fun contributInboxFragment(): InboxFragment

    @ContributesAndroidInjector
    abstract fun contributFaqFragment(): FaqFragment

    @ContributesAndroidInjector
    abstract fun contributHelpAndQuestionsFragment(): HelpAndQuestionsFragment

    @ContributesAndroidInjector
    abstract fun contributMapFragment(): MapFragment

    @ContributesAndroidInjector
    abstract fun contributManexPlusFragment(): ManexPlusFragment

    @ContributesAndroidInjector
    abstract fun contributWaitingManexFragment(): WaitingManexFragment

    @ContributesAndroidInjector
    abstract fun contributHomeSearchFragment(): HomeSearchFragment

    @ContributesAndroidInjector
    abstract fun contributEarnVoucherFragment(): EarnVoucherFragment

    @ContributesAndroidInjector
    abstract fun contributEarnVoucherDetailFragment(): EarnVoucherDetailFragment

    @ContributesAndroidInjector
    abstract fun contributEarnVoucherFilterFragment(): EarnVoucherFilterFragment

    @ContributesAndroidInjector
    abstract fun contributEarnVoucherSearchFragment(): EarnVoucherSearchFragment



}