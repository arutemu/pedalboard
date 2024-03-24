package com.mukuro.pedalboard.data

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable

@Immutable
data class Plugin(
    val id: Long,
    val type: PluginType = PluginType.ALL,
    val name: String,
    val aspectRatio: Float,
    val elements: List<PluginElement?>,
    val coverDrawable: Int? = null
)