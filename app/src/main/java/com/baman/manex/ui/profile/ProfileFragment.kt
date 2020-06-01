package com.baman.manex.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.controls.CustomRecyclerView
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.controls.CustomViewScrollCallBack
import com.baman.manex.data.dto.ProfileMenuDto
import com.baman.manex.data.dto.UserDto
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ProfileItemType
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.databinding.FragmentProfileBinding
import com.baman.manex.databinding.FragmentProfileCollapsinglayoutBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseActivity
import com.baman.manex.ui.baseClass.CollapsingToolbarFragment
import com.baman.manex.util.InternetConnection
import com.baman.manex.util.Preferences
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.snack.SnackHelper
import com.baman.manex.util.toPersianNumber
import com.bumptech.glide.request.RequestOptions
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.master.permissionhelper.PermissionHelper
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import com.zhihu.matisse.listener.OnCheckedListener
import com.zhihu.matisse.listener.OnSelectedListener
import kotlinx.android.synthetic.main.toolbar.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProfileFragment : CollapsingToolbarFragment(), Injectable, InternetConnection,
    CustomViewCallBack,
    CustomViewScrollCallBack {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentProfileBinding
    private lateinit var collapsingLayoutBinding: FragmentProfileCollapsinglayoutBinding
    private lateinit var viewModel: ProfileViewModel


    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen

    var supportContact = ""

    private var adapter: ProfileAdapter = ProfileAdapter {
        handleListClicks(it!!)
    }

    private var userDto = UserDto()

    private val REQUEST_CODE_CHOOSE = 23

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_profile, container, false)

        return binding.root
    }

    override fun onCreateCollapsingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        collapsingLayoutBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_profile_collapsinglayout, container, false)
        return collapsingLayoutBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ProfileViewModel::class.java)

        customRecyclerView = binding.customRecyclerView

        recyclerView = customRecyclerView.getRecyclerView()!!

        customRecyclerView.setCustomViewCallBack(this)
        customRecyclerView.setCustomViewScrollCallBack(this)
        customRecyclerView.setStatus(ListStatus.UNDEFINE)

        recyclerView?.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        skeletonScreen = Skeleton.bind(recyclerView)
            .adapter(adapter)
            .count(9)
            .load(R.layout.adapter_profile_skeleton)
            .shimmer(true)
            .show()

        getProfileItems()

        checkUserHasCard()

        getUserInfo()

        getWalletData()

        setAvatar()

        handleLayoutClick()

        getSupportContact()

    }

    private fun getSupportContact() {
        viewModel.supportContact.observe(this, Observer {
            it.handle(this, requireActivity()) { res, code ->
                supportContact = res.value
            }
        })
    }

    private fun handleLayoutClick() {
        collapsingLayoutBinding.waitingManexLayout.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.waitingManex())
        }

        collapsingLayoutBinding.profileImageUser.setOnClickListener {
            getStoragePermission()
        }

        collapsingLayoutBinding.clickableLayout.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.editProfile())
        }

        binding.myShoppingLayout.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.myShopping())
        }

        binding.textMytransaction.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.myTransaction())
        }

        binding.textMytransaction.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.myTransaction())
        }

        collapsingLayoutBinding.profileLayoutCard.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.addCard())
        }

        binding.textMyCards.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.myCards())
        }
    }

    private fun checkUserHasCard() {
        //check visibility of add card layout
        val userHasCard = Preferences.getUserHasCard(context!!)
        if (!userHasCard) {
            collapsingLayoutBinding.profileLayoutCard.visibility = View.VISIBLE
            binding.profileTopDivider.visibility = View.VISIBLE
        } else {
            collapsingLayoutBinding.profileLayoutCard.visibility = View.GONE
            binding.profileTopDivider.visibility = View.GONE
        }
    }

    private fun setAvatar() {
        viewModel.uploadAvatar.observe(this@ProfileFragment, Observer { it ->
            it.handle(ProfileFragment(), activity) { it, code ->
                viewModel.setFileId(it.id)
            }
        })

        viewModel.updateFileId.observe(this@ProfileFragment, Observer { it ->
            it.handle(ProfileFragment(), activity) { it, code ->
                if (!it.value.isNullOrBlank()) {
                    GlideApp.with(context!!).load(it.value)
                        .apply(RequestOptions.circleCropTransform())
                        .into(collapsingLayoutBinding.profileImageUser)

                    getToolbar().setUpIconFromUrl(it.value, true)

                }
            }
        })
    }

    private fun handleListClicks(profileMenuDto: ProfileMenuDto) {

        if (profileMenuDto.isEnabled) {
            when (ProfileItemType.Parse(profileMenuDto.code)) {
                ProfileItemType.UNDEFINE -> {
                    //
                }
                ProfileItemType.INVITE_FRIEND -> {
                    findNavController().navigate(ProfileFragmentDirections.inviteFriend())
                }
                ProfileItemType.MESSAGES -> {
                    findNavController().navigate(ProfileFragmentDirections.inbox())
                }
                ProfileItemType.MANEX_PLUS -> {
                    findNavController().navigate(ProfileFragmentDirections.manexPlus())
                }
                ProfileItemType.SUPPORT_CENTER -> {
                    if (supportContact.isNullOrEmpty()) {
                        SnackHelper.showSnack(
                            requireActivity(),
                            "خطایی رخ داده"
                        )
                    } else {
                        PublicFunction.actionCall(binding.root.context, supportContact)
                    }
                }
                ProfileItemType.HELP_AND_QUESTIONS -> {
                    findNavController().navigate(ProfileFragmentDirections.helpAndQuestions())
                }
                ProfileItemType.TERMS_AND_CONDITION -> {
                    findNavController().navigate(ProfileFragmentDirections.terms())
                }
                ProfileItemType.ABOUT_US -> {
                    findNavController().navigate(ProfileFragmentDirections.about())
                }
                ProfileItemType.COOPERATIONS -> {
                    findNavController().navigate(ProfileFragmentDirections.cooperation())
                }
                ProfileItemType.SETTING -> {
                    findNavController().navigate(ProfileFragmentDirections.setting())
                }
            }
        }
    }

    private fun getProfileItems() {
        viewModel.refresh()
        viewModel.menuData.observe(this, Observer { res ->
            res.handle(this, activity, skeletonScreen) { it, code ->

                customRecyclerView.setSwipeRefreshStatus(true)
                adapter.notifyDataSetChanged()
                customRecyclerView.setStatus(ListStatus.SUCCESS)

                adapter.setData(it)
            }
        })
    }

    private fun getWalletData() {
        viewModel.refresh()
        viewModel.walletData.observe(this, Observer { res ->
            res.handle(ProfileFragment(), requireActivity()) { it, code ->
                collapsingLayoutBinding.profileWaitingManex.text =
                    it.pendingManexAmount.toInt().toString().toPersianNumber()
                collapsingLayoutBinding.profileTextUserManex.text =
                    it.manexAmount.toInt().toString().toPersianNumber()
            }
        })
    }

    private fun getUserInfo() {
        viewModel.refresh()
        viewModel.getUser.observe(this@ProfileFragment, Observer { it ->
            it.handle(ProfileFragment(), activity) { it, code ->
                collapsingLayoutBinding.profileTextPhone.text = it.displayName?.toPersianNumber()

                val padding = resources.getDimensionPixelSize(R.dimen.toolbar_up_padding)
                getToolbar().image_up.setPadding(padding, padding, padding, padding)

                getToolbar().setTitle(it.displayName?.toPersianNumber().toString())

                if (!it.profileImageFileUrl.isNullOrBlank()) {
                    GlideApp.with(context!!).load(it.profileImageFileUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(collapsingLayoutBinding.profileImageUser)

                    getToolbar().setUpIconFromUrl(it.profileImageFileUrl.toString(), true)
                } else {
                    GlideApp.with(context!!).load(R.drawable.ic_male)
                        .apply(RequestOptions.circleCropTransform())
                        .into(getToolbar().image_up)
                }
                userDto = it
            }
        })
    }

    private fun getStoragePermission() {
        val permissionHelper = (context as BaseActivity)
            .getPermissionHelper(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 100
            )

        permissionHelper.request(object : PermissionHelper.PermissionCallback {
            override fun onPermissionGranted() {
                openImagePicker()

            }

            override fun onIndividualPermissionGranted(strings: Array<String>) {
            }

            override fun onPermissionDenied() {

            }

            override fun onPermissionDeniedBySystem() {
            }
        })
    }

    private fun openImagePicker() {
        Matisse.from(this)
            .choose(com.zhihu.matisse.MimeType.ofImage(), false)
            .countable(false)
            .capture(true)
            .captureStrategy(
                CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test")
            )
            .maxSelectable(1)
            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .setOnSelectedListener(OnSelectedListener { uriList: List<Uri?>?, pathList: List<String?> ->
                Log.e(
                    "onSelected",
                    "onSelected: pathList=$pathList"
                )
            })
            .showSingleMediaType(true)
            .originalEnable(false)
            .autoHideToolbarOnSingleTap(false)
            .setOnCheckedListener(OnCheckedListener { isChecked: Boolean ->
                Log.e(
                    "isChecked",
                    "onCheck: isChecked=$isChecked"
                )
            })
            .forResult(REQUEST_CODE_CHOOSE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {

            var b = Matisse.obtainPathResult(data)

            val file = File(b[0])

            var filePart = MultipartBody.Part.createFormData(
                "file",
                file.name, RequestBody.create(MediaType.parse("image/*"), file)
            )


            viewModel.setAvatarFile(filePart)

            Log.e("OnActivityResult ", Matisse.obtainOriginalState(data).toString())

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRetryClicked() {
        getProfileItems()
    }

    override fun onRefresh(page: Int) {
        getProfileItems()
    }

    override fun showNoConnection() {
        adapter.notifyDataSetChanged()
        if (::binding.isInitialized)
            binding.customRecyclerView.setStatus(ListStatus.CONNECTION_FAILED)
    }

    override fun onScrollChange(scrollDirection: ScrollDirection?) {

    }

}