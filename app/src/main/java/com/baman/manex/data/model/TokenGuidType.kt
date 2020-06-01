package com.baman.manex.data.model


enum class TokenGuidType constructor(val value: String, val code: Int) {

    NotSpecified("NotSpecified", 0),
    RegisteredCard("RegisteredCard", 1),
    Charge("Charge", 2),
    BuyVoucher("BuyVoucher", 4);

    companion object {

        fun Parse(value: String?): TokenGuidType {
            if (value == null) {
                return NotSpecified
            }
            val `arr$` = TokenGuidType.values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return NotSpecified
        }

        fun Parse(code: Int): TokenGuidType {
            if (code == 0) {
                return NotSpecified
            }
            val `arr$` = TokenGuidType.values()
            for (`val` in `arr$`) {
                if (`val`.code == code) {
                    return `val`
                }
            }
            return NotSpecified
        }
    }

}