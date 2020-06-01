package com.baman.manex.data.model

enum class UserTransactionStatus constructor(val value: String, val code: Int) {

    NotSpecified("NotSpecified", 0),
    Earn("Earn", 1),
    Waiting("Waiting", 2),
    Canceled("Canceled", 3);

    companion object {

        fun Parse(value: String?): UserTransactionStatus {
            if (value == null) {
                return NotSpecified
            }
            val `arr$` = UserTransactionStatus.values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return NotSpecified
        }

        fun Parse(code: Int): UserTransactionStatus {
            if (code == 0) {
                return NotSpecified
            }
            val `arr$` = UserTransactionStatus.values()
            for (`val` in `arr$`) {
                if (`val`.code == code) {
                    return `val`
                }
            }
            return NotSpecified
        }
    }

}
