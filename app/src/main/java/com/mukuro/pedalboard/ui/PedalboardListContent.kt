package com.mukuro.pedalboard.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.mukuro.pedalboard.R
/*


import com.mukuro.pedalboard.ui.components.ReplyEmailThreadItem*/
import com.mukuro.pedalboard.ui.components.PluginDetailAppBar
import com.mukuro.pedalboard.ui.components.PedalboardPluginCard
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.mukuro.pedalboard.data.Plugin
import com.mukuro.pedalboard.ui.utils.PedalboardContentType
import com.mukuro.pedalboard.ui.utils.PedalboardNavigationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedalboardQuickScreen(
    contentType: PedalboardContentType,
    pedalboardHomeUIState: PedalboardHomeUIState,
    navigationType: PedalboardNavigationType,
    displayFeatures: List<DisplayFeature>,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, PedalboardContentType) -> Unit,
    toggleSelectedPlugin: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    /**
     * When moving from LIST_AND_DETAIL page to LIST page clear the selection and user should see LIST screen.
     */
    LaunchedEffect(key1 = contentType) {
        if (contentType == PedalboardContentType.SINGLE_PANE && !pedalboardHomeUIState.isDetailOnlyOpen) {
            closeDetailScreen()
        }
    }

    val pluginLazyListState = rememberLazyListState()

    // TODO: Show top app bar over full width of app when in multi-select mode

    if (contentType == PedalboardContentType.DUAL_PANE) {
        TwoPane(
            first = {
                PedalboardPluginsList(
                    plugins = pedalboardHomeUIState.plugins,
                    openedPlugin = pedalboardHomeUIState.openedPlugin,
                    selectedPluginIds = pedalboardHomeUIState.selectedPlugins,
                    togglePluginSelection = toggleSelectedPlugin,
                    pluginLazyListState = pluginLazyListState,
                    navigateToDetail = navigateToDetail
                )
            },
            second = {
                PedalboardPluginDetail(
                    plugin = pedalboardHomeUIState.openedPlugin ?: pedalboardHomeUIState.plugins.first(),
                    isFullScreen = false
                )
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        ) ////////// refactored upper part, but it's logic is not working here, should REMOVE or CHANGE
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            PedalboardSinglePaneContent( // not needed???
                pedalboardHomeUIState = pedalboardHomeUIState,
                togglePluginSelection = toggleSelectedPlugin,
                pluginLazyListState = pluginLazyListState,
                modifier = Modifier.fillMaxSize(),
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail
            )
            // When we have bottom navigation we show FAB at the bottom end.
            if (navigationType == PedalboardNavigationType.BOTTOM_NAVIGATION) {
                LargeFloatingActionButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedalboardEffectsScreen(
    contentType: PedalboardContentType,
    pedalboardHomeUIState: PedalboardHomeUIState,
    navigationType: PedalboardNavigationType,
    displayFeatures: List<DisplayFeature>,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, PedalboardContentType) -> Unit,
    toggleSelectedPlugin: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    /**
     * When moving from LIST_AND_DETAIL page to LIST page clear the selection and user should see LIST screen.
     */
    LaunchedEffect(key1 = contentType) {
        if (contentType == PedalboardContentType.SINGLE_PANE && !pedalboardHomeUIState.isDetailOnlyOpen) {
            closeDetailScreen()
        }
    }

    val pluginLazyListState = rememberLazyListState()

    // TODO: Show top app bar over full width of app when in multi-select mode

    if (contentType == PedalboardContentType.DUAL_PANE) {
        TwoPane(
            first = {
                PedalboardPluginsList(
                    plugins = pedalboardHomeUIState.plugins,
                    openedPlugin = pedalboardHomeUIState.openedPlugin,
                    selectedPluginIds = pedalboardHomeUIState.selectedPlugins,
                    togglePluginSelection = toggleSelectedPlugin,
                    pluginLazyListState = pluginLazyListState,
                    navigateToDetail = navigateToDetail
                )
            },
            second = {
                PedalboardPluginDetail(
                    plugin = pedalboardHomeUIState.openedPlugin ?: pedalboardHomeUIState.plugins.first(),
                    isFullScreen = false
                )
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        ) ////////// refactored upper part, but it's logic is not working here, should REMOVE or CHANGE
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            PedalboardSinglePaneContent( // not needed???
                pedalboardHomeUIState = pedalboardHomeUIState,
                togglePluginSelection = toggleSelectedPlugin,
                pluginLazyListState = pluginLazyListState,
                modifier = Modifier.fillMaxSize(),
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail
            )
            // When we have bottom navigation we show FAB at the bottom end.
            if (navigationType == PedalboardNavigationType.BOTTOM_NAVIGATION) {
                LargeFloatingActionButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PedalboardSinglePaneContent(
    pedalboardHomeUIState: PedalboardHomeUIState,
    togglePluginSelection: (Long) -> Unit,
    pluginLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, PedalboardContentType) -> Unit
) {
    if (pedalboardHomeUIState.openedPlugin != null && pedalboardHomeUIState.isDetailOnlyOpen) {
        BackHandler {
            closeDetailScreen()
        }
        PedalboardPluginDetail(plugin = pedalboardHomeUIState.openedPlugin) {
            closeDetailScreen()
        }
    } else {
        PedalboardPluginsList(
            plugins = pedalboardHomeUIState.plugins,
            openedPlugin = pedalboardHomeUIState.openedPlugin,
            selectedPluginIds = pedalboardHomeUIState.selectedPlugins,
            togglePluginSelection = togglePluginSelection,
            pluginLazyListState = pluginLazyListState,
            modifier = modifier,
            navigateToDetail = navigateToDetail
        )
    }
}

@Composable
fun PedalboardPluginsList(
    plugins: List<Plugin>,
    openedPlugin: Plugin?,
    selectedPluginIds: Set<Long>,
    togglePluginSelection: (Long) -> Unit,
    pluginLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long, PedalboardContentType) -> Unit
) {
    // changed from COLUMN to ROW, let's check it
    Column() {
        // Insert Top Bar here? or not here?
        //PedalboardSearchBar(modifier = Modifier.fillMaxWidth()) // And replace this shit!
        //PedalboardTopBar(modifier = Modifier.fillMaxWidth(), onBackPressed = {}) // TODO - remove it from here? probably? Found a better place for it already :3
        LazyRow(
            modifier = modifier
                .clip(shape = RoundedCornerShape(36.dp,0.dp,0.dp,0.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
                //.padding(vertical = 12.dp),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            state = pluginLazyListState) {
/*        item {
            PedalboardSearchBar(modifier = Modifier.fillMaxWidth())
        }*/
            items(items = plugins, key = { it.id }) { plugin ->
                PedalboardPluginCard(
                    plugin = plugin,
                    navigateToDetail = { pluginId ->
                        navigateToDetail(pluginId, PedalboardContentType.SINGLE_PANE)
                    },
                    toggleSelection = togglePluginSelection,
                    isOpened = openedPlugin?.id == plugin.id,
                    isSelected = selectedPluginIds.contains(plugin.id)
                )
            }
        }
    }


}

@Composable
fun PedalboardPluginDetail(
    plugin: Plugin,
    isFullScreen: Boolean = true,
    modifier: Modifier = Modifier.fillMaxSize(),
    onBackPressed: () -> Unit = {}
) {
    // changed form COLUMN to ROW
    LazyRow(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(top = 16.dp)
    ) {
        item {
            PluginDetailAppBar(plugin, isFullScreen) {
                onBackPressed()
            }
        }
        /*items(items = plugin.threads, key = { it.id }) { plugin ->
            PedalboardPluginThreadItem(plugin = plugin)
        }*/
    }
}