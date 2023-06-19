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
            name = "test delay plugin"
        ),
        Plugin(
            id = 2L,
            type = PluginType.CHORUS,
            name = "test chorus plugin"
        ),
        Plugin(
            id = 3L,
            type = PluginType.DISTORTION,
            name = "test distortion plugin"
        ),
    )

    val allPlugins = listOf(
        Plugin(
            id = 1L,
            type = PluginType.DELAY,
            name = "Test delay"
        ),
        Plugin(
            id = 2L,
            type = PluginType.CHORUS,
            name = "Nayuta"
        ),
        Plugin(
            id = 3L,
            type = PluginType.DISTORTION,
            name = "Makima"
        ),
        Plugin(
            id = 4L,
            type = PluginType.DELAY,
            name = "Power"
        ),
        Plugin(
            id = 5L,
            type = PluginType.CHORUS,
            name = "Reze"
        ),
        Plugin(
            id = 6L,
            type = PluginType.DISTORTION,
            name = "test distortion plugin"
        ),
        Plugin(
            id = 7L,
            type = PluginType.DELAY,
            name = "test delay plugin"
        ),
        Plugin(
            id = 8L,
            type = PluginType.CHORUS,
            name = "test chorus plugin"
        ),
        Plugin(
            id = 9L,
            type = PluginType.DISTORTION,
            name = "test distortion plugin"
        )
    )

    fun get(id: Long): Plugin? {
        return allPlugins.firstOrNull {it.id == id}
    }

    fun create(): Plugin {
        return Plugin(
            id = 10L,
            type = PluginType.ALL,
            name = "new plugin"
        )
    }

    fun getAllTypes() = listOf(
        "delay",
        "chorus",
        "overdrive",
        "gain"
    )
}

