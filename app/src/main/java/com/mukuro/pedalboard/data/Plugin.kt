package com.mukuro.pedalboard.data

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment

@Immutable
data class Plugin(
    val id: Long,
    val type: PluginType = PluginType.ALL,
    val name: String,
    val aspectRatio: Float,
    val elements: List<PluginElement?>,
    val coverDrawable: Int? = null,
    val horizontal: Arrangement.Horizontal? = Arrangement.Center,
    val vertical: Arrangement.Vertical? = Arrangement.Top
)