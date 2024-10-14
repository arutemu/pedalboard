package com.mukuro.pedalboard.data

import androidx.compose.runtime.Immutable

/**
 * TODO:
 * 1 - add selector
 * 2 - add anything else */

@Immutable
open class PluginElement(
    open val id: Long,
    open val name: String
)

data class Knob(
    override val id: Long,
    override val name: String,
    val startPoint: Float,
    val endPoint: Float,
    val measure: String
) : PluginElement(id, name)

data class Switch(
    override val id: Long,
    override val name: String,
    val state: Boolean
) : PluginElement(id, name)

data class Slider(
    override val id: Long,
    override val name: String,
    val startValue: Float,
    val endValue: Float,
    //val value: Float,
    val measure: String
) : PluginElement(id, name)

data class RangeSlider(
    override val id: Long,
    override val name: String,
    val startValue: Float,
    val endValue: Float,
    val measure: String
) : PluginElement(id, name)