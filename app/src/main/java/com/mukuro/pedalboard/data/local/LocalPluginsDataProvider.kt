package com.mukuro.pedalboard.data.local

import com.mukuro.pedalboard.R
import com.mukuro.pedalboard.data.Plugin
import com.mukuro.pedalboard.data.PluginType

/**
 * A static data store of [Plugin]s.
 */

object LocalPluginsDataProvider {

    private val pluginSet = listOf(
        Plugin(
            id = 1L,
            type = PluginType.DELAY,
            name = "test delay plugin",
            aspectRatio = 1.2f
        ),
        Plugin(
            id = 2L,
            type = PluginType.CHORUS,
            name = "test chorus plugin",
            aspectRatio = 0.6f
        ),
        Plugin(
            id = 3L,
            type = PluginType.DISTORTION,
            name = "test distortion plugin",
            aspectRatio = 0.6f
        ),
    )

    val allPlugins = listOf(
        Plugin(
            id = 1L,
            type = PluginType.DELAY,
            name = "Test delay",
            aspectRatio = 0.6f
        ),
        Plugin(
            id = 2L,
            type = PluginType.CHORUS,
            name = "Nayuta 2",
            aspectRatio = 0.4f
        ),
        Plugin(
            id = 3L,
            type = PluginType.DISTORTION,
            name = "Makima 2",
            aspectRatio = 0.6f
        ),
        Plugin(
            id = 4L,
            type = PluginType.DELAY,
            name = "Power 2",
            aspectRatio = 0.8f
        ),
        Plugin(
            id = 5L,
            type = PluginType.CHORUS,
            name = "Reze 2",
            aspectRatio = 0.9f
        ),
        Plugin(
            id = 6L,
            type = PluginType.DISTORTION,
            name = "test distortion plugin",
            aspectRatio = 0.6f
        ),
        Plugin(
            id = 7L,
            type = PluginType.DELAY,
            name = "test delay plugin",
            aspectRatio = 0.6f
        ),
        Plugin(
            id = 8L,
            type = PluginType.CHORUS,
            name = "test chorus plugin",
            aspectRatio = 0.2f
        ),
        Plugin(
            id = 9L,
            type = PluginType.DISTORTION,
            name = "test distortion plugin",
            aspectRatio = 0.5f
        )
    )

    fun get(id: Long): Plugin? {
        return allPlugins.firstOrNull {it.id == id}
    }

    fun create(): Plugin {
        return Plugin(
            id = 10L,
            type = PluginType.ALL,
            name = "new plugin",
            aspectRatio = 0.6f
        )
    }

    fun getAllTypes() = listOf(
        "delay",
        "chorus",
        "overdrive",
        "gain"
    )
}

