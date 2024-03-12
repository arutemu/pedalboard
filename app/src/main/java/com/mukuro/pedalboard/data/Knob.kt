package com.mukuro.pedalboard.data

data class Knob(
    val id: Long,
    val name: String,
    val startPoint: Float,
    val endPoint: Float,
    val measure: String
)