package com.mukuro.pedalboard.data

import androidx.compose.runtime.Immutable

@Immutable
data class Preset(
    val id: Long,
    val name: String,
    var isStarred: Boolean = false,
    //val author: String = "",
    val plugins: List<Plugin> = emptyList(),
)