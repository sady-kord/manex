package com.baman.manex.util.snack


import androidx.annotation.LongDef

@LongDef(
    SnackLength.LENGTH_SHORT,
    SnackLength.LENGTH_LONG
)
@Retention(AnnotationRetention.SOURCE)
annotation class SnackLength {
    companion object {
        const val LENGTH_SHORT = 2_000L

        const val LENGTH_LONG = 3_500L

        const val LENGTH_INDEFINITE = -1L
    }
}