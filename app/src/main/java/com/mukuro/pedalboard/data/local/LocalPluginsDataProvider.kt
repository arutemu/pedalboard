package com.mukuro.pedalboard.data.local

import com.mukuro.pedalboard.R
import com.mukuro.pedalboard.data.Knob
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
            aspectRatio = 1.2f,
            elements = TestKnob.knobSet1
        ),
        Plugin(
            id = 2L,
            type = PluginType.CHORUS,
            name = "test chorus plugin",
            aspectRatio = 0.6f,
            elements = TestKnob.knobSet2
        ),
        Plugin(
            id = 3L,
            type = PluginType.DISTORTION,
            name = "test distortion plugin",
            aspectRatio = 0.6f,
            elements = TestKnob.knobSet1
        ),
    )

    val allPlugins = listOf(
        Plugin(
            id = 1L,
            type = PluginType.DELAY,
            name = "Test delay",
            aspectRatio = 0.6f,
            elements = TestKnob.knobSet1
        ),
        Plugin(
            id = 2L,
            type = PluginType.CHORUS,
            name = "Nayuta 2",
            aspectRatio = 0.4f,
            elements = TestKnob.knobSet2
        ),
        Plugin(
            id = 3L,
            type = PluginType.DISTORTION,
            name = "Makima 2",
            aspectRatio = 0.6f,
            elements = TestKnob.knobSet1
        ),
        Plugin(
            id = 4L,
            type = PluginType.DELAY,
            name = "Power 2",
            aspectRatio = 0.8f,
            elements = TestKnob.knobSet2
        ),
        Plugin(
            id = 5L,
            type = PluginType.CHORUS,
            name = "Reze 2",
            aspectRatio = 0.9f,
            elements = TestKnob.knobSet1
        ),
        Plugin(
            id = 6L,
            type = PluginType.DISTORTION,
            name = "test distortion plugin",
            aspectRatio = 0.6f,
            elements = TestKnob.knobSet2
        ),
        Plugin(
            id = 7L,
            type = PluginType.DELAY,
            name = "test delay plugin",
            aspectRatio = 0.6f,
            elements = TestKnob.knobSet1
        ),
        Plugin(
            id = 8L,
            type = PluginType.CHORUS,
            name = "test chorus plugin",
            aspectRatio = 0.2f,
            elements = TestKnob.knobSet2
        ),
        Plugin(
            id = 9L,
            type = PluginType.DISTORTION,
            name = "test distortion plugin",
            aspectRatio = 0.5f,
            elements = TestKnob.knobSet1
        )
    )


    object TestKnob {
        val knobSet1 = listOf(
            Knob(
                id = 1,
                name = "Volume",
                startPoint = 0f,
                endPoint = 100f,
                measure = "db"
            ),
            Knob(
                id = 2,
                name = "Gain",
                startPoint = -10f,
                endPoint = 20f,
                measure = "db"
            ),
            Knob(
                id = 3,
                name = "Drive",
                startPoint = 0f,
                endPoint = 100f,
                measure = "db"
            )
        )
        val knobSet2 = listOf(
            Knob(
                id = 1,
                name = "Volume",
                startPoint = 0f,
                endPoint = 100f,
                measure = "db"
            ),
            Knob(
                id = 2,
                name = "Gain",
                startPoint = -10f,
                endPoint = 20f,
                measure = "db"
            ),
            Knob(
                id = 3,
                name = "Drive",
                startPoint = 0f,
                endPoint = 100f,
                measure = "db"
            ),
            Knob(
                id = 4,
                name = "Overdrive",
                startPoint = 0f,
                endPoint = 100f,
                measure = "db"
            )

        )

    }

    fun get(id: Long): Plugin? {
        return allPlugins.firstOrNull {it.id == id}
    }

    fun create(): Plugin {
        return Plugin(
            id = 10L,
            type = PluginType.ALL,
            name = "new plugin",
            aspectRatio = 0.6f,
            elements = TestKnob.knobSet1
        )
    }

    fun getAllTypes() = listOf(
        "delay",
        "chorus",
        "overdrive",
        "gain"
    )
}

