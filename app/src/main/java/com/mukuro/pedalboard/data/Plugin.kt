package com.mukuro.pedalboard.data

import androidx.window.embedding.EmbeddingAspectRatio

data class Plugin(
    val id: Long,
    val type: PluginType = PluginType.ALL,
    val name: String,
    val aspectRatio: Float
)