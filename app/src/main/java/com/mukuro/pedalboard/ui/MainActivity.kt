package com.mukuro.pedalboard.ui

/*import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mukuro.pedalboard.ui.theme.PedalboardTheme*/

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mukuro.pedalboard.data.local.LocalPluginsDataProvider
import com.mukuro.pedalboard.ui.theme.PedalboardTheme
import com.google.accompanist.adaptive.calculateDisplayFeatures


/*
 TODO - everything
    1. PluginDetailAppBar
                 partially done 2. PedalboardPluginThreadItem >>>>>> plugin > threads (LocalPluginsDataProvider)
                        3. LocalPluginsDataProvider >>> PluginsRepositoryImpl >>> PedalboardHomeViewModel
*/


class MainActivity : ComponentActivity() {

    private val viewModel: PedalboardHomeViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PedalboardTheme {
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                PedalboardApp(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    pedalboardHomeUIState = uiState,
                    closeDetailScreen = {
                        viewModel.closeDetailScreen()
                    },
                    navigateToDetail = { pluginId, pane ->
                        viewModel.setOpenedPlugin(pluginId, pane)
                    },
                    toggleSelectedPlugin = { pluginId ->
                        viewModel.toggleSelectedPlugin(pluginId)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
fun PedalboardAppPreview() {
    PedalboardTheme {
        PedalboardApp(
            pedalboardHomeUIState = PedalboardHomeUIState(plugins = LocalPluginsDataProvider.allPlugins),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(400.dp, 600.dp)),
            displayFeatures = emptyList(),
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun PedalboardAppPreviewTablet() {
    PedalboardTheme {
        PedalboardApp(
            pedalboardHomeUIState = PedalboardHomeUIState(plugins = LocalPluginsDataProvider.allPlugins),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(700.dp, 500.dp)),
            displayFeatures = emptyList(),
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 500, heightDp = 700)
@Composable
fun PedalboardAppPreviewTabletPortrait() {
    PedalboardTheme {
        PedalboardApp(
            pedalboardHomeUIState = PedalboardHomeUIState(plugins = LocalPluginsDataProvider.allPlugins),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(500.dp, 700.dp)),
            displayFeatures = emptyList(),
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 1100, heightDp = 600)
@Composable
fun PedalboardAppPreviewDesktop() {
    PedalboardTheme {
        PedalboardApp(
            pedalboardHomeUIState = PedalboardHomeUIState(plugins = LocalPluginsDataProvider.allPlugins),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(500.dp, 700.dp)),
            displayFeatures = emptyList(),
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 600, heightDp = 1100)
@Composable
fun PedalboardAppPreviewDesktopPortrait() {
    PedalboardTheme {
        PedalboardApp(
            pedalboardHomeUIState = PedalboardHomeUIState(plugins = LocalPluginsDataProvider.allPlugins),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(500.dp, 700.dp)),
            displayFeatures = emptyList(),
        )
    }
}