package com.baman.manex.ui.profile.editProfile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baman.manex.R
import com.baman.manex.data.dto.UserDto
import com.baman.manex.data.dto.ValidationDto
import com.baman.manex.data.model.GenderType
import com.baman.manex.databinding.FragmentEditProfileBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.baseClass.BaseFragment
import com.baman.manex.ui.profile.ProfileViewModel
import com.baman.manex.util.*
import com.baman.manex.util.snack.SnackHelper
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import ir.hamsaa.persiandatepicker.util.PersianCalendarConstants
import kotlinx.android.synthetic.main.control_text_input_layout_profile.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import javax.inject.Inject


class EditProfileFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding by autoCleared<FragmentEditProfileBinding>()

    private lateinit var viewModel: ProfileViewModel

    var validationDto: ValidationDto? = null

    private var userDto = UserDto()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_edit_profile, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ProfileViewModel::class.java)


        viewModel.validation.observe(this, Observer {

            validationDto = it

            if (it.address!!.required!!)
                binding.addressTextinputprofile.setMaxLenght(it.address!!.maxLength!!)

            if (it.postalcode!!.required!!)
                binding.postalCodeTextinputprofile.setMaxLenght(it.postalcode!!.length!!)

            if (it.email!!.required!!)
                binding.emailTextinputprofile.setMaxLenght(it.email!!.maxLength!!)

        })

        toolbar.setTitle("اطلاعات پروفایل")
        toolbar.showUpIcon(true) {

            KeyBoardHelper(requireActivity()).closeKeyBoard()
            findNavController().navigateUp()

        }

        toolbar.setFongroundTintResource(R.color.black)

        binding.nameTextinputprofile.requestFocus()
        binding.nameTextinputprofile.clearFocus()
        binding.postalCodeTextinputprofile.setNumericKeyboard()
        binding.postalCodeTextinputprofile.setMaxLenght(10)

        binding.birthdayTextinputprofile.frame.setOnClickListener {

            var picker = PersianDatePickerDialog(requireContext())
                .setPositiveButtonString("تایید")
                .setNegativeButton("لغو")
                .setTodayButtonVisible(false)
                .setMinYear(1300)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setActionTextColor(Color.GRAY)
                .setTypeFace(ResourcesCompat.getFont(requireContext(), R.font.iranyekan))
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(object : Listener {
                    override fun onDismissed() {

                    }

                    override fun onDateSelected(persianCalendar: PersianCalendar?) {
                        setDate(
                            persianCalendar!!.persianYear, persianCalendar.persianMonth,
                            persianCalendar.persianDay
                        )
                    }
                })

            picker.show()

        }

        binding.genderTextinputprofile.frame.setOnClickListener {

            var gender = 0
            if (!binding.genderTextinputprofile.getValue().isNullOrEmpty()) {
                gender = GenderType.Parse(binding.genderTextinputprofile.getValue()).code
            }

            val genderDialogFragment =
                GenderDialogFragment.newInstance(
                    GenderType.Parse(gender).code
                )

            genderDialogFragment.setCallBack(object :
                GenderDialogFragment.DialogCallBack {
                override fun onCallBack(key: String) {
                    binding.genderTextinputprofile.setValue(key)
                }
            })

            genderDialogFragment.show(childFragmentManager, "")

        }

        getUserInfo()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    SnackHelper?.dismiss()
                    isEnabled = false

                    findNavController().navigateUp()
                }
            })

        binding.saveLayout.setOnClickListener {

            KeyBoardHelper(requireActivity()).closeKeyBoard()

            if (checkValidation()) {
                userDto.firstName = binding.nameTextinputprofile.getValue()
                userDto.lastName = binding.familyTextinputprofile.getValue()
                userDto.email = binding.emailTextinputprofile.getValue()
                userDto.gender = GenderType.Parse(binding.genderTextinputprofile.getValue()).code
                userDto.jobTitle = binding.jobTextinputprofile.getValue()
                userDto.address = binding.addressTextinputprofile.getValue()
                userDto.postalCode = binding.postalCodeTextinputprofile.getValue()

                viewModel.setUserDto(userDto)

                viewModel.updateUser.observe(this@EditProfileFragment, Observer {
                    it.handle(
                        this, activity, null,
                        null, null,binding.saveLayout, binding.loading
                    ) {it,code->
                        binding.loading.visibility = View.GONE
                        binding.saveLayout.visibility = View.VISIBLE
                        findNavController().navigateUp()
                    }
                })
            }
        }

    }

    private fun checkValidation(): Boolean {
        var height = 60f
        if (DeviceStatus.hasSoftKeys(requireActivity().windowManager, requireContext()))
            height = DeviceStatus.getNavBarHeight(requireContext()).toFloat()

//        if (binding.nameTextinputprofile.getValue().isEmpty()) {
//            SnackHelper.showSnack(requireActivity(), "لطفا نام را وارد کنید", height)
//            return false
//        }
//        if (binding.familyTextinputprofile.getValue().isEmpty()) {
//            SnackHelper.showSnack(requireActivity(), "لطفا نام خانوادگی را وارد کنید", height)
//            return false
//        }
//        if (binding.emailTextinputprofile.getValue().isEmpty()) {
//            SnackHelper.showSnack(requireActivity(), "لطفا ایمیل را وارد کنید", height)
//            return false
//        }

        if (validationDto != null) {
            if (validationDto!!.email!!.required!!) {
                if (binding.emailTextinputprofile.getValue().length < validationDto!!.email!!.minLength!!)
                    SnackHelper.showSnack(requireActivity(), "لطفا ایمیل معتبر وارد کنید", height)
                return false
            }
        }

        if (binding.emailTextinputprofile.getValue().isNotEmpty() && !PublicFunction.isValidEmailAddress(
                binding.emailTextinputprofile.getValue()
            )
        ) {
            SnackHelper.showSnack(requireActivity(), "لطفا ایمیل معتبر وارد کنید", height)
            return false
        }

        if (binding.postalCodeTextinputprofile.getValue().isNotEmpty() && !PublicFunction.isValidPostCode(
                binding.postalCodeTextinputprofile.getValue()
            )
        ) {
            SnackHelper.showSnack(requireActivity(), "لطفا کد پستی معتبر وارد کنید", height)
            return false
        }

        return true
    }

    private fun getUserInfo() {
        viewModel.getUser.observe(this@EditProfileFragment, Observer { it ->
            it.handle(this, activity) {it,code->

                userDto = it

                if (!it.firstName.isNullOrBlank())
                    binding.nameTextinputprofile.setValue(it.firstName)

                if (!it.lastName.isNullOrBlank())
                    binding.familyTextinputprofile.setValue(it.lastName)

                if (!it.email.isNullOrBlank())
                    binding.emailTextinputprofile.setValue(it.email)

                if (!it.jobTitle.isNullOrBlank())
                    binding.jobTextinputprofile.setValue(it.jobTitle)

                if (!it.address.isNullOrBlank())
                    binding.addressTextinputprofile.setValue(it.address)

                if (!it.postalCode.isNullOrBlank())
                    binding.postalCodeTextinputprofile.setValue(it.postalCode)

                if (it.year != null && it.year != 0 &&
                    it.month != null && it.month != 0 &&
                    it.day != null && it.day != 0
                ) {
                    setDate(it.year!!, it.month!!, it.day!!)
                }

                if (it.gender != null || it.gender != 0)
                    binding.genderTextinputprofile.setValue(GenderType.Parse(it.gender!!).value)

            }
        })
    }

    fun setDate(year: Int, month: Int, day: Int) {


        var monthName: String = PersianCalendarConstants.persianMonthNames[month - 1]

        binding.birthdayTextinputprofile.setValue("${day.toString().toPersianNumber()} $monthName ${year.toString().toPersianNumber()}")

        userDto.day = day
        userDto.month = month
        userDto.year = year
    }

}