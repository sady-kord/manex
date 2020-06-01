package com.baman.manex.data.dto

import com.baman.manex.data.model.ProductType

sealed class BuyRequestDto(

    var isBasic: Boolean? = false,

    var page: Int? = null,

    var canBuy: Boolean? = null,

    var allSearchAddress: Boolean? = null,

    var allSearchName: Boolean? = null,

    var minValue: Int? = null,

    var maxValue: Int? = null,

    var searchText: String? = null,

    var tags: List<Int>? = null,

    var sortItem: String? = null ,

    var productType : ProductType? = null
)

class VoucherRequest(page : Int) : BuyRequestDto(
    false, page, null, null, null,
    null, null, null, null, "ManexOffer" , ProductType.Voucher
)

class ShopRequest(page : Int) : BuyRequestDto(
    true, page, null, null, null,
    null, null, null, null, "ManexOffer" , ProductType.Good
)