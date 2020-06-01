package com.baman.manex.util.ext

import android.content.res.Resources


fun Resources.pixelsToSp(px: Float): Float = px / displayMetrics.scaledDensity