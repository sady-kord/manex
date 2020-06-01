package com.baman.manex.data.model


enum class UserBuyStatus constructor(val value: String, val code: Int) {

    NotSpecified("NotSpecified", 0),
    Init("Init", 1),
    WaitForSend("WaitForSend", 2),
    UnSuccess("UnSuccess", 3),
    Sent("Sent", 4),
    Deliverd("Deliverd", 5),
    Expired("Expired", 6),
    Valid("Valid", 7);

    companion object {

        fun Parse(value: String?): UserBuyStatus {
            if (value == null) {
                return NotSpecified
            }
            val `arr$` = UserBuyStatus.values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return NotSpecified
        }

        fun Parse(code: Int): UserBuyStatus {
            if (code == 0) {
                return NotSpecified
            }
            val `arr$` = UserBuyStatus.values()
            for (`val` in `arr$`) {
                if (`val`.code == code) {
                    return `val`
                }
            }
            return NotSpecified
        }
    }

}