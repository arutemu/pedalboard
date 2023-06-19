package com.mukuro.pedalboard.data

data class Plugin(
    val id: Long,
    val type: PluginType = PluginType.ALL,
    val name: String
)