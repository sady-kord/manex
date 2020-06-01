package com.baman.manex.network.service

import androidx.lifecycle.LiveData
import com.baman.manex.data.dto.FileUploadDto
import com.baman.manex.data.dto.FriendDto
import com.baman.manex.data.dto.UpdateAvatarFileDto
import com.baman.manex.network.ApiResponse
import com.zhihu.matisse.internal.entity.IncapableCause
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface FileService {

    @Multipart
    @POST("file/upload/avatar")
    fun uploadAvatar(@Part file:MultipartBody.Part ): LiveData<ApiResponse<FileUploadDto>>


    @PUT("auth/userprofile/updateavatarfileid")
    fun updateAvatarFileId(@Query("fileId") fileId:Long): LiveData<ApiResponse<UpdateAvatarFileDto>>

}