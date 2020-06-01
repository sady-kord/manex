package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.FileUploadDto
import com.baman.manex.data.dto.UpdateAvatarFileDto
import com.baman.manex.network.Resource
import com.baman.manex.network.service.FileService
import okhttp3.MultipartBody
import javax.inject.Inject


class FileRepository @Inject constructor(
    private val fileService: FileService
) {

    fun upload(file : MultipartBody.Part ): LiveData<Resource<FileUploadDto>> =
        OnlineResource(fileService.uploadAvatar(file)).asLiveData()

    fun updateFileId(fileId : Long): LiveData<Resource<UpdateAvatarFileDto>> =
        OnlineResource(fileService.updateAvatarFileId(fileId)).asLiveData()
}
