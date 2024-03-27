package com.mukuro.pedalboard.data.local

import androidx.compose.foundation.layout.Arrangement
import com.mukuro.pedalboard.R
import com.mukuro.pedalboard.data.Knob
import com.mukuro.pedalboard.data.PluginElement
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
            name = "test Nayuta plugin",
            aspectRatio = 0.6f,
            elements = TestKnob.knobSet1,
            coverDrawable = R.drawable.nayuta
        ),
        Plugin(
            id = 2L,
            type = PluginType.CHORUS,
            name = "test chorus plugin",
            aspectRatio = 0.6f,
            elements = TestKnob.knobSet2,
            coverDrawable = R.drawable.power
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
            name = "Amptweaker Small Form",
            aspectRatio = 0.581f,
            elements = TestKnob.knobSet1,
            coverDrawable = R.drawable.anime_girl_3,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 2L,
            type = PluginType.CHORUS,
            name = "Boss single switch",
            aspectRatio = 0.574f,
            elements = TestKnob.knobSet2,
            coverDrawable = R.drawable.illustration_1,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 3L,
            type = PluginType.DISTORTION,
            name = "Boss double switch",
            aspectRatio = 1.088f,
            elements = TestKnob.knobSet3,
            coverDrawable = R.drawable.makima,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 4L,
            type = PluginType.DELAY,
            name = "DigiTech Whammy",
            aspectRatio = 0.839f,
            elements = TestKnob.knobSet2,
            coverDrawable = R.drawable.frieren_1,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 5L,
            type = PluginType.CHORUS,
            name = "EHX Big Muff",
            aspectRatio = 0.809f,
            elements = TestKnob.knobSet1,
            coverDrawable = R.drawable.nayuta,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 6L,
            type = PluginType.DISTORTION,
            name = "EHX Small Clone",
            aspectRatio = 0.647f,
            elements = TestKnob.knobSet2,
            coverDrawable = R.drawable.madoka_2,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 7L,
            type = PluginType.DELAY,
            name = "Ibanez Tube Screamer Classic super duper long name",
            aspectRatio = 0.542f,
            elements = TestKnob.knobSet3,
            coverDrawable = R.drawable.anime_girl_4,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 8L,
            type = PluginType.CHORUS,
            name = "Line 6 Modeler Pedals",
            aspectRatio = 1.667f,
            elements = TestKnob.knobSet2,
            coverDrawable = R.drawable.makima_6,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 9L,
            type = PluginType.DISTORTION,
            name = "Small MXR Pedals",
            aspectRatio = 0.535f,
            elements = TestKnob.knobSet1,
            coverDrawable = R.drawable.nayuta_1,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 10L,
            type = PluginType.CHORUS,
            name = "Morley Wah Pedals",
            aspectRatio = 1.553f,
            elements = TestKnob.knobSet3,
            coverDrawable = R.drawable.madoka_1
        ),
        Plugin(
            id = 11L,
            type = PluginType.CHORUS,
            name = "ProCo Rat Two",
            aspectRatio = 0.875f,
            elements = TestKnob.knobSet2,
            coverDrawable = R.drawable.makima_7,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 12L,
            type = PluginType.CHORUS,
            name = "Strymon Small Form",
            aspectRatio = 0.889f,
            elements = TestKnob.knobSet1,
            coverDrawable = R.drawable.illustration_3
        ),
        Plugin(
            id = 13L,
            type = PluginType.CHORUS,
            name = "Strymon Large Form",
            aspectRatio = 1.324f,
            elements = TestKnob.knobSet3,
            coverDrawable = R.drawable.illustration_2
        ),
        Plugin(
            id = 14L,
            type = PluginType.CHORUS,
            name = "TC Electronic Mini",
            aspectRatio = 0.514f,
            elements = TestKnob.knobSet2,
            coverDrawable = R.drawable.bocchi_1,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 15L,
            type = PluginType.CHORUS,
            name = "TC Electronic Single Stomp",
            aspectRatio = 0.583f,
            elements = TestKnob.knobSet3,
            coverDrawable = R.drawable.bocchi_2,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 16L,
            type = PluginType.CHORUS,
            name = "Way Huge",
            aspectRatio = 0.798f,
            elements = TestKnob.knobSet2,
            coverDrawable = R.drawable.hutao_1,
            horizontal = Arrangement.Center,
            vertical = Arrangement.Bottom
        ),
        Plugin(
            id = 17L,
            type = PluginType.CHORUS,
            name = "Walrus Audio Large Form",
            aspectRatio = 1.217f,
            elements = TestKnob.knobSet2,
            coverDrawable = R.drawable.crymachina_1
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
        val knobSet3 = listOf(
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
            ),
            Knob(
                id = 5,
                name = "Sustain",
                startPoint = 0f,
                endPoint = 50f,
                measure = "ms"
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

