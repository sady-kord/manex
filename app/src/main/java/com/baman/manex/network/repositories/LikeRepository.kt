package com.baman.manex.network.repositories

import com.baman.manex.data.dto.LikeDto
import com.baman.manex.network.service.LikeService
import javax.inject.Inject


class LikeRepository  @Inject constructor(
    private val likeService: LikeService
) {

    fun setBranchLike(id : Long)=
        OnlineResource(likeService.setBranchLike(LikeDto(id))).asLiveData()

    fun setVoucherLike(id : Long)=
        OnlineResource(likeService.setVoucherLike(LikeDto(id))).asLiveData()

    fun setShopLike(id : Long)=
        OnlineResource(likeService.setShopLike(LikeDto(id))).asLiveData()
}

