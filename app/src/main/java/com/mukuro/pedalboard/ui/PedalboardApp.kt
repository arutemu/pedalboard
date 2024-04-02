package com.mukuro.pedalboard.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature    // remove
import com.mukuro.pedalboard.ui.components.PedalboardTopBar

//import com.mukuro.pedalboard.ui.navigation.ModalNavigationDrawerContent
import com.mukuro.pedalboard.ui.navigation.PedalboardBottomNavigationBar
import com.mukuro.pedalboard.ui.navigation.PedalboardNavigationRail

import com.mukuro.pedalboard.ui.navigation.DismissibleNavigationDrawerContent
import com.mukuro.pedalboard.ui.navigation.ModalNavigationDrawerContent
import com.mukuro.pedalboard.ui.navigation.PedalboardNavigationActions
import com.mukuro.pedalboard.ui.navigation.PedalboardRoute // WIP - needs fixes with icons
import com.mukuro.pedalboard.ui.navigation.PedalboardTopLevelDestination
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
            navigationType = PedalboardNavigationType.DISMISSIBLE_NAVIGATION_DRAWER
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

/*    Column(modifier = Modifier){
        PedalboardTopBar(onDrawerClicked = { })

        Box(modifier = Modifier) {

            Row(modifier = Modifier){
                Column(modifier = Modifier.width(80.dp)){ }
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

            PedalboardNavigationRail(
                selectedDestination = "Quick",
                navigationContentPosition = PedalboardNavigationContentPosition.CENTER ,
                navigateToTopLevelDestination = {}
            )

        }
    }*/

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
    //val isNavigationRailVisible = remember { mutableStateOf(true) }

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        PedalboardNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: PedalboardRoute.QUICK


    // TODO - move NavRail here
    /*    PedalboardNavigationRail(
            selectedDestination = selectedDestination,
            navigationContentPosition = navigationContentPosition,
            navigateToTopLevelDestination = navigateToTopLevelDestination,
            onDrawerClicked = onDrawerClicked
        )*/


    Column(modifier = Modifier){
        PedalboardTopBar(onDrawerClicked = {
            if (drawerState.isOpen) {
                scope.launch {
                    drawerState.close()
                }
            }
            else {
                scope.launch {
                    drawerState.open()
                }
            }
        })

        Box(modifier = Modifier) {

            Row(modifier = Modifier){
                Column(modifier = Modifier.width(80.dp)){ }
                if (navigationType == PedalboardNavigationType.DISMISSIBLE_NAVIGATION_DRAWER) { // TODO probably modify the condition and layout for mobile screens (should cover Column above too)

                    // TODO check on custom width of PermanentNavigationDrawer: b/232495216
                    DismissibleNavigationDrawer(drawerState = drawerState, drawerContent = {
                        DismissibleNavigationDrawerContent(
                            selectedDestination = selectedDestination,
                            navigationContentPosition = navigationContentPosition,
                            navigateToTopLevelDestination = navigationActions::navigateTo,
                            drawerState = drawerState
                        )
                    }
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
                            toggleSelectedPlugin = toggleSelectedPlugin,
                            onDrawerClicked =  {
                                if (drawerState.isOpen) {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                                else {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            },
                            isNavigationRailVisible = true, // TODO - probably should remove this property at all
                            drawerState = drawerState
                        )

                    }
                } else {
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
                            toggleSelectedPlugin = toggleSelectedPlugin,
                            onDrawerClicked = {
                                if (drawerState.isOpen) {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                                else {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            },
                            isNavigationRailVisible = false,
                            drawerState = drawerState
                        )
                    }
                }
            }




            PedalboardNavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = PedalboardNavigationContentPosition.CENTER ,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                onDrawerClicked = {
                    if (drawerState.isOpen) {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                    else {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                }
            )



        }
    }



    // Change 3
    //if (navigationType == ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER) {

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
    onDrawerClicked: () -> Unit = {},
    isNavigationRailVisible: Boolean,
    drawerState: DrawerState
) {
    //TEST STRING, feel free to delete
    val scope = rememberCoroutineScope()
    //val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    //Row(modifier = modifier.fillMaxSize()) {

        // Possible place to insert the action bar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {

            /** TODO - try to move Top App bar here
                also - maybe it should be in NEW PedalboardTopNavHost??
             */

            //PedalboardTopBar(onDrawerClicked = {})
            PedalboardNavHost(
                navController = navController,
                contentType = contentType,
                displayFeatures = displayFeatures,
                pedalboardHomeUIState = pedalboardHomeUIState,
                navigationType = navigationType,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                toggleSelectedPlugin = toggleSelectedPlugin,
                modifier = Modifier.weight(1f), //.clip(shape = RoundedCornerShape(45.dp,0.dp,0.dp,0.dp))
            )
            AnimatedVisibility(visible = navigationType == PedalboardNavigationType.BOTTOM_NAVIGATION) {
                PedalboardBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }



    //}
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
            //EmptyComingSoon()
            PedalboardEffectsScreen(
                contentType = contentType,
                pedalboardHomeUIState = pedalboardHomeUIState,
                navigationType = navigationType,
                displayFeatures = displayFeatures,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                toggleSelectedPlugin = toggleSelectedPlugin
            )
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
