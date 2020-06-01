package com.baman.manex.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baman.manex.dao.ValidationDao
import com.baman.manex.data.dto.*
import com.baman.manex.network.Resource
import com.baman.manex.network.repositories.*
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    userRepository: UserRepository,
    walletRepository: WalletRepository,
    fileRepository: FileRepository,
    menuRepository: MenuRepository,
    validationDao: ValidationDao,
    manexSupportRepository: ManexSupportRepository
) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()

    private var userDto = MutableLiveData<UserDto>()
    private var avatarFile = MutableLiveData<MultipartBody.Part >()
    private var fileId = MutableLiveData<Long>()

    val walletData: LiveData<Resource<WalletDto>> = Transformations
        .switchMap(reloadTrigger) {
            walletRepository.getUserManex()
        }

    val supportContact = manexSupportRepository.getSupportContactData()

    val validation = validationDao.findValidation()

    val menuData : LiveData<Resource<List<ProfileMenuDto>>> = Transformations
        .switchMap(reloadTrigger){
            menuRepository.getMenu()
        }

    var getUser: LiveData<Resource<UserDto>> =
        Transformations.switchMap(reloadTrigger) {
            userRepository.getUserInfo()
        }

    var updateUser: LiveData<Resource<UpdateUserDto>> = Transformations
        .switchMap(userDto) { input ->
            userRepository.updateUser(input)
        }

    fun setUserDto(userDto: UserDto) {
        this.userDto.postValue(userDto)
    }

    var uploadAvatar: LiveData<Resource<FileUploadDto>> = Transformations
        .switchMap(avatarFile) { input ->
            fileRepository.upload(input)
        }

    fun setAvatarFile(file: MultipartBody.Part ) {
        this.avatarFile.postValue(file)
    }

    var updateFileId: LiveData<Resource<UpdateAvatarFileDto>> = Transformations
        .switchMap(fileId) { input ->
            fileRepository.updateFileId(input)
        }

    fun setFileId(fileId: Long) {
        this.fileId.postValue(fileId)
    }

    fun refresh(){
        reloadTrigger.value = true
    }

}