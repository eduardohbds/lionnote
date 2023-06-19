package com.example.lionnote

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

data class MainItem(
    val id: Int,
    @DrawableRes val drawbleId: Int,
    @StringRes val textStringId: Int,
    val color: Int
)
