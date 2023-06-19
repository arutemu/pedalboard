package com.mukuro.pedalboard.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature    // remove
import androidx.window.layout.FoldingFeature    // remove

//import com.mukuro.pedalboard.ui.navigation.ModalNavigationDrawerContent
import com.mukuro.pedalboard.ui.navigation.PedalboardBottomNavigationBar
import com.mukuro.pedalboard.ui.navigation.PedalboardNavigationRail

import com.mukuro.pedalboard.ui.navigation.DismissibleNavigationDrawerContent
import com.mukuro.pedalboard.ui.navigation.PedalboardNavigationActions
import com.mukuro.pedalboard.ui.navigation.PedalboardRoute // WIP - needs fixes with icons
import com.mukuro.pedalboard.ui.navigation.PedalboardTopLevelDestination
import com.mukuro.pedalboard.ui.utils.DevicePosture
import com.mukuro.pedalboard.ui.utils.isBookPosture
import com.mukuro.pedalboard.ui.utils.isSeparating
import com.mukuro.pedalboard.ui.utils.PedalboardContentType
import com.mukuro.pedalboard.ui.utils.PedalboardNavigationContentPosition
import com.mukuro.pedalboard.ui.utils.PedalboardNavigationType

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedalboardApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    pedalboardHomeUIState: PedalboardHomeUIState,
    closeDetailScreen: () -> Unit = {},
    navigateToDetail: (Long, PedalboardContentType) -> Unit = { _, _ -> },
    toggleSelectedPlugin: (Long) -> Unit = { }
) {
    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val navigationType: PedalboardNavigationType
    val contentType: PedalboardContentType

    /*    BackHandler(enabled = drawerState.isOpen) {
            scope.launch {
                drawerState.close()
            }
        }*/
    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */

    // Modified code - used to be for foldables, now nope

    /*    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

        val foldingDevicePosture = when {
            isBookPosture(foldingFeature) ->
                DevicePosture.BookPosture(foldingFeature.bounds)

            isSeparating(foldingFeature) ->
                DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

            else -> DevicePosture.NormalPosture
        }*/

    // Also modified - removed foldable options and DUAL PANE, maybe can use dual for something else?
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = PedalboardNavigationType.BOTTOM_NAVIGATION
            contentType = PedalboardContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = PedalboardNavigationType.NAVIGATION_RAIL
            contentType = PedalboardContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = PedalboardNavigationType.DISMISSABLE_NAVIGATION_DRAWER
            contentType = PedalboardContentType.SINGLE_PANE
        }
        else -> {
            navigationType = PedalboardNavigationType.BOTTOM_NAVIGATION
            contentType = PedalboardContentType.SINGLE_PANE
        }
    }

    /**
     * Content inside Navigation Rail/Drawer can also be positioned at top, bottom or center for
     * ergonomics and reachability depending upon the height of the device.
     */
    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            PedalboardNavigationContentPosition.TOP
        }
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            PedalboardNavigationContentPosition.TOP // changed from CENTER
        }
        else -> {
            PedalboardNavigationContentPosition.TOP
        }
    } ///// FUCK THIS CODE, LEAVE IT ON TOP!!!!1 TODO - refactor ^ that shit, it's almost useless

    PedalboardNavigationWrapper(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        pedalboardHomeUIState = pedalboardHomeUIState,
        closeDetailScreen = closeDetailScreen,
        navigateToDetail = navigateToDetail,
        toggleSelectedPlugin = toggleSelectedPlugin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PedalboardNavigationWrapper(
    navigationType: PedalboardNavigationType,
    contentType: PedalboardContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: PedalboardNavigationContentPosition,
    pedalboardHomeUIState: PedalboardHomeUIState,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, PedalboardContentType) -> Unit,
    toggleSelectedPlugin: (Long) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        PedalboardNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: PedalboardRoute.QUICK

    // Change 3
    //if (navigationType == ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER) {
    if (navigationType == PedalboardNavigationType.DISMISSABLE_NAVIGATION_DRAWER) {
        /*val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val selectedItem = remember { mutableStateOf(TOP_LEVEL_DESTINATIONS[0]) }*/

        // TODO check on custom width of PermanentNavigationDrawer: b/232495216
        //Change5 Permanent > Dismis
        //PermanentNavigationDrawer(drawerContent = {

        DismissibleNavigationDrawer(drawerContent = {
            // Change 5+
            DismissibleNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                /////////////////////
                drawerState = drawerState//rememberDrawerState(DrawerValue.Open)

                // Need to close it by clicking on icon
            )
        }) {
            PedalboardAppContent(
                navigationType = navigationType,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                pedalboardHomeUIState = pedalboardHomeUIState,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                toggleSelectedPlugin = toggleSelectedPlugin
            )
        }
    } /*else {
        ModalNavigationDrawer(
            //gesturesEnabled = false,
            drawerContent = {
                ModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigationContentPosition = navigationContentPosition,
                    navigateToTopLevelDestination = navigationActions::navigateTo,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            drawerState = drawerState
        ) {
            PedalboardAppContent(
                navigationType = navigationType,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                pedalboardHomeUIState = pedalboardHomeUIState,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                toggleSelectedEmail = toggleSelectedEmail
            ) {
                scope.launch {
                    drawerState.open()
                }
            }
        }
    }*/
}

@Composable
fun PedalboardAppContent(
    modifier: Modifier = Modifier,
    navigationType: PedalboardNavigationType,
    contentType: PedalboardContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: PedalboardNavigationContentPosition,
    pedalboardHomeUIState: PedalboardHomeUIState,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (PedalboardTopLevelDestination) -> Unit,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, PedalboardContentType) -> Unit,
    toggleSelectedPlugin: (Long) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = modifier.fillMaxSize()) {
        //////////////////// Added shit here - second condition, probably not the best idea
        AnimatedVisibility(visible = navigationType == PedalboardNavigationType.NAVIGATION_RAIL || navigationType == PedalboardNavigationType.DISMISSABLE_NAVIGATION_DRAWER ) {
            PedalboardNavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = onDrawerClicked
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            PedalboardNavHost(
                navController = navController,
                contentType = contentType,
                displayFeatures = displayFeatures,
                pedalboardHomeUIState = pedalboardHomeUIState,
                navigationType = navigationType,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                toggleSelectedPlugin = toggleSelectedPlugin,
                modifier = Modifier.weight(1f),
            )
            AnimatedVisibility(visible = navigationType == PedalboardNavigationType.BOTTOM_NAVIGATION) {
                PedalboardBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }
    }
}

@Composable
private fun PedalboardNavHost(
    navController: NavHostController,
    contentType: PedalboardContentType,
    displayFeatures: List<DisplayFeature>,
    pedalboardHomeUIState: PedalboardHomeUIState,
    navigationType: PedalboardNavigationType,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, PedalboardContentType) -> Unit,
    toggleSelectedPlugin: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = PedalboardRoute.QUICK,
    ) {
        composable(PedalboardRoute.QUICK) {
            PedalboardQuickScreen(
                contentType = contentType,
                pedalboardHomeUIState = pedalboardHomeUIState,
                navigationType = navigationType,
                displayFeatures = displayFeatures,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                toggleSelectedPlugin = toggleSelectedPlugin
            )
        }
        composable(PedalboardRoute.EFFECTS) {
            EmptyComingSoon()
        }
        composable(PedalboardRoute.PRESETS) {
            EmptyComingSoon()
        }
        composable(PedalboardRoute.RECORDED) {
            EmptyComingSoon()
        }
        composable(PedalboardRoute.DRUMS) {
            EmptyComingSoon()
        }
    }
}
