package com.mukuro.pedalboard.ui

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.window.layout.DisplayFeature
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
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
import my.nanihadesuka.compose.InternalLazyRowScrollbar
import my.nanihadesuka.compose.LazyRowScrollbar
import my.nanihadesuka.compose.ScrollbarLayoutSide
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings

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
                PedalboardAllPluginsList(
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
    // Trying to use Coil here
     //res.getStringArray(R.array.planets_array)


    // changed from COLUMN to ROW, let's check it
    Column(
        modifier = modifier
            //.padding(horizontal = 12.dp) // TODO - need to make the clip modifier to be used only on tablets
            .clip(shape = RoundedCornerShape(36.dp, 0.dp, 0.dp, 0.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        // Insert Top Bar here? or not here?
        //PedalboardSearchBar(modifier = Modifier.fillMaxWidth()) // And replace this shit!
        //PedalboardTopBar(modifier = Modifier.fillMaxWidth(), onBackPressed = {}) // TODO - remove it from here? probably? Found a better place for it already :3
        // For Glide

        val state = rememberLazyListState()
        // Values for Scrollbar
        val listData = (0..1000).toList()
        //val listState = rememberLazyListState()
/*        LazyRowScrollbar(
            state = pluginLazyListState,
            settings = ScrollbarSettings(
                selectionMode = ScrollbarSelectionMode.Full,
                side = ScrollbarLayoutSide.End,
                alwaysShowScrollbar = true,
                //scrollbarPadding = 12.dp,
                //thumbThickness = 12.dp,
                thumbMinLength = 0.2f
            )
        ) {*/
            LazyRow(
                modifier = Modifier
                    .fillMaxHeight(fraction = 0.95f),
                /*modifier = modifier
                    .clip(shape = RoundedCornerShape(36.dp, 0.dp, 0.dp, 0.dp))
                    .background(MaterialTheme.colorScheme.surfaceDim), */// color is fucked after libs update
                //.padding(vertical = 12.dp),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                state = pluginLazyListState
            ) {
/*        item {
            PedalboardSearchBar(modifier = Modifier.fillMaxWidth())
        }*/
                items(items = plugins, key = { it.id }) { plugin ->
                    PedalboardPluginCard(
                        plugin = plugin,
                        isOpened = openedPlugin?.id == plugin.id,
                        isSelected = selectedPluginIds.contains(plugin.id)
                    )
                }
            }
            InternalLazyRowScrollbar(
                modifier = Modifier
                    .fillMaxHeight(),
                    //.padding(horizontal = 24.dp),
                state = pluginLazyListState,
                settings = ScrollbarSettings(
                    selectionMode = ScrollbarSelectionMode.Full,
                    side = ScrollbarLayoutSide.End,
                    alwaysShowScrollbar = true,
                    //scrollbarPadding = 12.dp,
                    //thumbThickness = 36.dp,
                    thumbMinLength = 0.3f
                ),
                // TODO - implement custom indicatorContent for custom scrollbar design :3
                //indicatorContent = Scrollbar()
            )
        }

    //}
}

@Composable
fun PedalboardAllPluginsList(
    plugins: List<Plugin>,
    openedPlugin: Plugin?,
    selectedPluginIds: Set<Long>,
    togglePluginSelection: (Long) -> Unit,
    pluginLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long, PedalboardContentType) -> Unit
) {
    // Trying to use Coil here
    //res.getStringArray(R.array.planets_array)


    // changed from COLUMN to ROW, let's check it
    Column() {
        // Insert Top Bar here? or not here?
        //PedalboardSearchBar(modifier = Modifier.fillMaxWidth()) // And replace this shit!
        //PedalboardTopBar(modifier = Modifier.fillMaxWidth(), onBackPressed = {}) // TODO - remove it from here? probably? Found a better place for it already :3
        // For Glide
        val state = rememberLazyListState()

        LazyRow(
            modifier = modifier
                .clip(shape = RoundedCornerShape(36.dp, 0.dp, 0.dp, 0.dp))
                .background(MaterialTheme.colorScheme.surfaceDim), // color is fucked after libs update
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

@Composable
fun ImageLoader(plugin: Plugin) {
    val uri = Uri.parse("android.resource://com.mukuro.pedalboard/" + "R.drawable." + plugin.name.lowercase().replace(" ", "_") + ".jpg") //plugin.name.lowercase().replace(" ", "_") + ".jpg"
    //val painter = re
    Image(
        painter = rememberAsyncImagePainter(uri),
        contentDescription = "cover image",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize()
    )
}

// OLD STUFF, DOES NOT WORK, IS NOT USED, REMOVE IT!
@Composable
fun Scrollbar() {
    Card(
        modifier = Modifier
            .height(16.dp)
            .width(120.dp)
            .padding(vertical = 8.dp)
            .clip(CardDefaults.shape),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(),
        content = {}
    )
}
