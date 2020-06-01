package com.baman.manex.data.dto

import com.baman.manex.data.model.RequestType

sealed class RequestDto(

    var isBasic: Boolean? = false,

    var page: Int? = null,

    var canBuy: Boolean? = null,

    var allSearchAddress: Boolean? = null,

    var allSearchName: Boolean? = null,

    var minValue: Int? = null,

    var maxValue: Int? = null,

    var searchText: String? = null,

    var tags: List<Int>? = null,

    var sortItem: Int? = null ,

    var VoucherUseType : RequestType? = null
) {
    fun isEqual(requestDto: RequestDto): Boolean {


        if (isBasic != null){
            if (requestDto.isBasic == null){
                return false
            }else{
                if (isBasic!! != requestDto.isBasic) {
                    return false
                }
            }
        }else{
            if (requestDto.isBasic != null){
                return false
            }
        }

        if (canBuy != null){
            if (requestDto.canBuy == null){
                return false
            }else{
                if (canBuy!! != requestDto.canBuy) {
                    return false
                }
            }
        }else{
            if (requestDto.isBasic != null){
                return false
            }
        }

        if (page != null){
            if (requestDto.page == null){
                return false
            }else{
                if (page!! != requestDto.page) {
                    return false
                }
            }
        }else{
            if (requestDto.page != null){
                return false
            }
        }

        if (allSearchAddress != null){
            if (requestDto.allSearchAddress == null){
                return false
            }else{
                if (allSearchAddress!! != requestDto.allSearchAddress) {
                    return false
                }
            }
        }else{
            if (requestDto.allSearchAddress != null){
                return false
            }
        }

        if (allSearchName != null){
            if (requestDto.allSearchName == null){
                return false
            }else{
                if (allSearchName!! != requestDto.allSearchName) {
                    return false
                }
            }
        }else{
            if (requestDto.allSearchName != null){
                return false
            }
        }

        if (minValue != null){
            if (requestDto.minValue == null){
                return false
            }else{
                if (minValue!! != requestDto.minValue) {
                    return false
                }
            }
        }else{
            if (requestDto.minValue != null){
                return false
            }
        }

        if (maxValue != null){
            if (requestDto.maxValue == null){
                return false
            }else{
                if (maxValue!! != requestDto.maxValue) {
                    return false
                }
            }
        }else{
            if (requestDto.maxValue != null){
                return false
            }
        }

        if (tags != null){
            if (requestDto.tags == null){
                return false
            }else{
                if (tags!! != requestDto.tags) {
                    return false
                }
            }
        }else{
            if (requestDto.tags != null){
                return false
            }
        }

//        if (sortItem != null){
//            if (requestDto.sortItem == null){
//                return false
//            }else{
//                if (sortItem!! != requestDto.sortItem) {
//                    return false
//                }
//            }
//        }else{
//            if (requestDto.sortItem != null){
//                return false
//            }
//        }

        if (searchText != null){
            if (requestDto.searchText == null){
                return false
            }else{
                if (searchText!! != requestDto.searchText) {
                    return false
                }
            }
        }else{
            if (requestDto.searchText != null){
                return false
            }
        }


        return true
    }
}

class BasicRequest : RequestDto(
    true, null, null, null, null,
    null, null, null, null, null , null
)

class BasicEarnRequest : RequestDto(true, null, null, null, null,
    null, null, null, null, null , RequestType.Earn)

class BasicBurnRequest : RequestDto(true, null, null, null, null,
    null, null, null, null, null , RequestType.Burn)

class ShowMoreRequest(page: Int, sortItem: Int?=null) : RequestDto(
    null, page, null, null, null,
    null, null, null, null, sortItem , null
)

class ShowMoreBurnRequest(page: Int, sortItem: Int?=null) : RequestDto(
    null, page, null, null, null,
    null, null, null, null, sortItem , RequestType.Burn
)

class ShowMoreEarnRequest(page: Int, sortItem: Int?=null) : RequestDto(
    null, page, null, null, null,
    null, null, null, null, sortItem , RequestType.Earn
)

class SearchRequest(query: String) : RequestDto(
    null, null, null, null, null,
    null, null, query, null, null
)

class SearchEarnRequest(query: String) : RequestDto(
    null, null, null, null, null,
    null, null, query, null, null,RequestType.Earn
)

class SearchBurnRequest(query: String) : RequestDto(
    null, null, null, null, null,
    null, null, query, null, null,RequestType.Burn
)


class MoreSearchRequest(
    query: String,
    page : Int,
    nameSearch: Boolean? = false,
    addressSearch: Boolean? = false
) : RequestDto(
    null, page, null, addressSearch, nameSearch,
    null, null, query, null, null
)

class SearchTagRequest(page: Int, tags: List<Int>) : RequestDto(
    false, page, null, null, null,
    null, null, null, tags, null
)

class SearchTagEarnRequest(page: Int, tags: List<Int>) : RequestDto(
    false, page, null, null, null,
    null, null, null, tags, null,RequestType.Earn
)

class SearchTagBurnRequest(page: Int, tags: List<Int>) : RequestDto(
    false, page, null, null, null,
    null, null, null, tags, null,RequestType.Burn
)

class InitialFilterRequest(min: Int, max: Int) : RequestDto(
    null, 1, false,
    null, null, min, max,
    null, null, null
)

class InitialFilterEarnRequest(min: Int, max: Int) : RequestDto(
    null, 1, false,
    null, null, min, max,
    null, null, null,RequestType.Earn
)

class InitialFilterBurnRequest(min: Int, max: Int) : RequestDto(
    null, 1, false,
    null, null, min, max,
    null, null, null,RequestType.Burn
)

