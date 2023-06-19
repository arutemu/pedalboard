package com.mukuro.pedalboard.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.mukuro.pedalboard.R

object PedalboardRoute {
    // TODO - define proper values and understand where exactly and HOW this is used
    const val QUICK = "Quick"
    const val EFFECTS = "Effects"
    const val PRESETS = "Presets"
    const val RECORDED = "Recorded"
    const val DRUMS = "Drums"
}

data class PedalboardTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

class PedalboardNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: PedalboardTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

/*
TODO
    1 - replace icons with a correct ones
*/

val TOP_LEVEL_DESTINATIONS = listOf(
    PedalboardTopLevelDestination(
        route = PedalboardRoute.QUICK,
        selectedIcon = Icons.Default.AddCircle,
        unselectedIcon = Icons.Default.AddCircle,
        iconTextId = R.string.tab_quick
    ),
    PedalboardTopLevelDestination(
        route = PedalboardRoute.EFFECTS,
        selectedIcon = Icons.Default.PlayArrow,
        unselectedIcon = Icons.Default.PlayArrow,
        iconTextId = R.string.tab_effects
    ),
    PedalboardTopLevelDestination(
        route = PedalboardRoute.PRESETS,
        selectedIcon = Icons.Outlined.ArrowForward,
        unselectedIcon = Icons.Outlined.ArrowForward,
        iconTextId = R.string.tab_presets
    ),
    PedalboardTopLevelDestination(
        route = PedalboardRoute.RECORDED,
        selectedIcon = Icons.Default.ArrowBack,
        unselectedIcon = Icons.Default.ArrowBack,
        iconTextId = R.string.tab_recorded
    ),
    PedalboardTopLevelDestination(
        route = PedalboardRoute.DRUMS,
        selectedIcon = Icons.Default.Star,
        unselectedIcon = Icons.Default.Star,
        iconTextId = R.string.tab_drums
    )

)