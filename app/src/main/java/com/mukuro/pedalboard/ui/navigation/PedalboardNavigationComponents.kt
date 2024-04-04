@file:OptIn(ExperimentalMaterial3Api::class)

// TODO -
package com.mukuro.pedalboard.ui.navigation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.DismissibleDrawerSheet
//import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.sp
import com.mukuro.pedalboard.R
import com.mukuro.pedalboard.ui.utils.PedalboardNavigationContentPosition
import kotlinx.coroutines.launch

@Composable
fun PedalboardNavigationRail(
    selectedDestination: String,
    navigationContentPosition: PedalboardNavigationContentPosition,
    navigateToTopLevelDestination: (PedalboardTopLevelDestination) -> Unit
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        // May insert header  here // header =
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        // TODO - connect powerOn to actual engine start
        var powerOn: Boolean by rememberSaveable { mutableStateOf(false) }
        // TODO - remove custom nav rail positioning when NavRail component supports it. ticket : b/232495216
        Layout(
            modifier = Modifier.widthIn(max = 80.dp),
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId(LayoutType.HEADER),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) { /** Old Nav.Drawer icon was here */
/*                    NavigationRailItem(
                        selected = false,
                        // Experiment
                        onClick = onDrawerClicked,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(id = R.string.navigation_drawer)
                            )
                        }
                    )*/
                    val fabColor by animateColorAsState(if (powerOn) Color(0xFF5EC281) else Color(0xffc25e66), label = "Power State")
                    val iconColor by animateColorAsState(if (powerOn) Color(0xcc00643b) else Color(0xff871055), label = "Power State")
                    FloatingActionButton(
                        onClick = { powerOn = !powerOn },
                        modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
                        containerColor = fabColor, //MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = stringResource(id = R.string.power),
                            modifier = Modifier.size(18.dp),
                            tint = iconColor
                        )
                    }
                    Spacer(Modifier.height(8.dp)) // NavigationRailHeaderPadding
                    Spacer(Modifier.height(4.dp)) // NavigationRailVerticalPadding
                }

                Column(
                    modifier = Modifier.layoutId(LayoutType.CONTENT),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TOP_LEVEL_DESTINATIONS.forEach { replyDestination ->
                        NavigationRailItem(
                            selected = selectedDestination == replyDestination.route,
                            onClick = { navigateToTopLevelDestination(replyDestination) },
                            icon = {
                                Icon(
                                    imageVector = replyDestination.selectedIcon,
                                    contentDescription = stringResource(
                                        id = replyDestination.iconTextId
                                    )
                                )
                            }
                        )
                    }
                }
            },
            measurePolicy = { measurables, constraints ->
                lateinit var headerMeasurable: Measurable
                lateinit var contentMeasurable: Measurable
                measurables.forEach {
                    when (it.layoutId) {
                        LayoutType.HEADER -> headerMeasurable = it
                        LayoutType.CONTENT -> contentMeasurable = it
                        else -> error("Unknown layoutId encountered!")
                    }
                }

                val headerPlaceable = headerMeasurable.measure(constraints)
                val contentPlaceable = contentMeasurable.measure(
                    constraints.offset(vertical = -headerPlaceable.height)
                )
                layout(constraints.maxWidth, constraints.maxHeight) {
                    // Place the header, this goes at the top
                    headerPlaceable.placeRelative(0, 0)

                    // Determine how much space is not taken up by the content
                    val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

                    val contentPlaceableY = when (navigationContentPosition) {
                        // Figure out the place we want to place the content, with respect to the
                        // parent (ignoring the header for now)
                        PedalboardNavigationContentPosition.TOP -> 0
                        PedalboardNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
                    }
                        // And finally, make sure we don't overlap with the header.
                        .coerceAtLeast(headerPlaceable.height)

                    contentPlaceable.placeRelative(0, contentPlaceableY)
                }
            }
        )
    }
}

@Composable
fun PedalboardBottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (PedalboardTopLevelDestination) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { replyDestination ->
            NavigationBarItem(
                selected = selectedDestination == replyDestination.route,
                onClick = { navigateToTopLevelDestination(replyDestination) },
                icon = {
                    Icon(
                        imageVector = replyDestination.selectedIcon,
                        contentDescription = stringResource(id = replyDestination.iconTextId)
                    )
                }
            )
        }
    }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermanentNavigationDrawerContent(
    selectedDestination: String,
    navigationContentPosition: ReplyNavigationContentPosition,
    navigateToTopLevelDestination: (PedalboardTopLevelDestination) -> Unit,
) {
    PermanentDrawerSheet(modifier = Modifier.sizeIn(minWidth = 200.dp, maxWidth = 300.dp)) {
        // TODO remove custom nav drawer content positioning when NavDrawer component supports it. ticket : b/232495216
        Layout(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(16.dp),
            content = {
                Column(
                    modifier = Modifier.layoutId(LayoutType.HEADER),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(id = R.string.app_name).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    ExtendedFloatingActionButton(
                        onClick = { *//*TODO*//* },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 40.dp),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(id = R.string.edit),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.compose),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.CONTENT)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TOP_LEVEL_DESTINATIONS.forEach { replyDestination ->
                        NavigationDrawerItem(
                            selected = selectedDestination == replyDestination.route,
                            label = {
                                Text(
                                    text = stringResource(id = replyDestination.iconTextId),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = replyDestination.selectedIcon,
                                    contentDescription = stringResource(
                                        id = replyDestination.iconTextId
                                    )
                                )
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = Color.Transparent
                            ),
                            onClick = { navigateToTopLevelDestination(replyDestination) }
                        )
                    }
                }
            },
            measurePolicy = { measurables, constraints ->
                lateinit var headerMeasurable: Measurable
                lateinit var contentMeasurable: Measurable
                measurables.forEach {
                    when (it.layoutId) {
                        LayoutType.HEADER -> headerMeasurable = it
                        LayoutType.CONTENT -> contentMeasurable = it
                        else -> error("Unknown layoutId encountered!")
                    }
                }

                val headerPlaceable = headerMeasurable.measure(constraints)
                val contentPlaceable = contentMeasurable.measure(
                    constraints.offset(vertical = -headerPlaceable.height)
                )
                layout(constraints.maxWidth, constraints.maxHeight) {
                    // Place the header, this goes at the top
                    headerPlaceable.placeRelative(0, 0)

                    // Determine how much space is not taken up by the content
                    val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

                    val contentPlaceableY = when (navigationContentPosition) {
                        // Figure out the place we want to place the content, with respect to the
                        // parent (ignoring the header for now)
                        ReplyNavigationContentPosition.TOP -> 0
                        ReplyNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
                    }
                        // And finally, make sure we don't overlap with the header.
                        .coerceAtLeast(headerPlaceable.height)

                    contentPlaceable.placeRelative(0, contentPlaceableY)
                }
            }
        )
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalNavigationDrawerContent(
    selectedDestination: String,
    navigationContentPosition: PedalboardNavigationContentPosition,
    navigateToTopLevelDestination: (PedalboardTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    ModalDrawerSheet {
        // TODO remove custom nav drawer content positioning when NavDrawer component supports it. ticket : b/232495216
        Layout(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(16.dp),
            content = {
                Column(
                    modifier = Modifier.layoutId(LayoutType.HEADER),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_name).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(onClick = onDrawerClicked) {
                            Icon(
                                imageVector = Icons.Default.MenuOpen,
                                contentDescription = stringResource(id = R.string.navigation_drawer)
                            )
                        }
                    }

                    ExtendedFloatingActionButton(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 40.dp),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(id = R.string.edit),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.compose),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.CONTENT)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TOP_LEVEL_DESTINATIONS.forEach { replyDestination ->
                        NavigationDrawerItem(
                            selected = selectedDestination == replyDestination.route,
                            label = {
                                Text(
                                    text = stringResource(id = replyDestination.iconTextId),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = replyDestination.selectedIcon,
                                    contentDescription = stringResource(
                                        id = replyDestination.iconTextId
                                    )
                                )
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = Color.Transparent
                            ),
                            onClick = { navigateToTopLevelDestination(replyDestination) }
                        )
                    }
                }
            },
            measurePolicy = { measurables, constraints ->
                lateinit var headerMeasurable: Measurable
                lateinit var contentMeasurable: Measurable
                measurables.forEach {
                    when (it.layoutId) {
                        LayoutType.HEADER -> headerMeasurable = it
                        LayoutType.CONTENT -> contentMeasurable = it
                        else -> error("Unknown layoutId encountered!")
                    }
                }

                val headerPlaceable = headerMeasurable.measure(constraints)
                val contentPlaceable = contentMeasurable.measure(
                    constraints.offset(vertical = -headerPlaceable.height)
                )
                layout(constraints.maxWidth, constraints.maxHeight) {
                    // Place the header, this goes at the top
                    headerPlaceable.placeRelative(0, 0)

                    // Determine how much space is not taken up by the content
                    val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

                    val contentPlaceableY = when (navigationContentPosition) {
                        // Figure out the place we want to place the content, with respect to the
                        // parent (ignoring the header for now)
                        PedalboardNavigationContentPosition.TOP -> 0
                        PedalboardNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
                    }
                        // And finally, make sure we don't overlap with the header.
                        .coerceAtLeast(headerPlaceable.height)

                    contentPlaceable.placeRelative(0, contentPlaceableY)
                }
            }
        )
    }
}

// EXPERIMENT HERE

// Adding Top app bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    //actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    //colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    scrollBehavior: () -> Unit
) {
    return
}

fun customShape() =  object : Shape { // testing block for changing width of NavDrawer
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(Rect(0f,0f,300f /* width */, 131f /* height */))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissibleNavigationDrawerContent( // TODO - remove obsolete stuff
    selectedDestination: String,
    navigationContentPosition: PedalboardNavigationContentPosition,
    navigateToTopLevelDestination: (PedalboardTopLevelDestination) -> Unit,
    drawerState: DrawerState,// = rememberDrawerState(DrawerValue.Closed),
    gesturesEnabled: Boolean = false,
    onDrawerClicked: () -> Unit = {}
) {
    // GOVNOKOD
    //val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    //val selectedItem = remember { mutableStateOf(TOP_LEVEL_DESTINATIONS[0]) }
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    // Experimental stuff for Dropdown menus
    // TODO replace options with real values (in the far future)
    val context = LocalContext.current
    val inputOptions = arrayOf("Input 1", "Input 2", "Input 3", "Input 4")
    var expandedInput by remember { mutableStateOf(false) }
    var selectedInput by remember { mutableStateOf(inputOptions[0]) }
    val outputOptions = arrayOf("Output 1", "Output 2", "Output 3", "Output 4")
    var expandedOutput by remember { mutableStateOf(false) }
    var selectedOutput by remember { mutableStateOf(inputOptions[0]) }


    DismissibleDrawerSheet() { //// Want to make it smaller, but UI gets fucked then, fix it then add >>> modifier = Modifier.requiredWidth(300.dp)
        // TODO remove custom nav drawer content positioning when NavDrawer component supports it. ticket : b/232495216
        Layout(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(16.dp),
                //.requiredWidth(300.dp),
            content = {

                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.HEADER)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // ORIGINAL CODE
                    // ADDED CODE

                    Text(
                        text = stringResource(id = R.string.quick_settings), //.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary //primary,
                        //fontStyle = FontStyle.Italic,
                        //fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.layoutId(LayoutType.CONTENT),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // ORIGINAL CODE
                    // ADDED CODE

                    // Mixer
                    Text(
                        text = "   " + stringResource(id = R.string.mixer), //.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Italic
                    )

                    Card(
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        elevation = CardDefaults.cardElevation( // TODO - maybe elevation is not good looking in this panel...
                            defaultElevation = 3.dp
                        )
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(modifier = Modifier.weight(0.5f)) {
                                Box(modifier = Modifier
                                    .width(48.dp)
                                    .fillMaxHeight()
                                    .background(MaterialTheme.colorScheme.secondary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .rotate(-90f),
                                        text = "In",
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                                VerticalDivider(
                                    modifier = Modifier.fillMaxHeight(),
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.inverseOnSurface
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 8.dp),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Slider(
                                        modifier = Modifier.fillMaxWidth(),
                                        state = SliderState(0f),
                                        colors = SliderDefaults.colors(inactiveTrackColor = MaterialTheme.colorScheme.surfaceBright),
                                        /*thumb = {
                                                    SliderDefaults.Thumb(
                                                        interactionSource = interactionSource,
                                                        colors = SliderDefaults.colors(thumbColor = Color.Transparent)
                                                        //enabled = enabled
                                                }*/
                                    )
                                    Slider(
                                        modifier = Modifier.fillMaxWidth(),
                                        state = SliderState(1f),
                                        colors = SliderDefaults.colors(inactiveTrackColor = MaterialTheme.colorScheme.surfaceBright)
                                    )
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.inverseOnSurface
                            )
                            Row(modifier = Modifier.weight(0.5f)) {
                                Box(
                                    modifier = Modifier
                                        .width(48.dp)
                                        .fillMaxHeight()
                                        .background(MaterialTheme.colorScheme.secondary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .rotate(-90f),
                                        text = "Out",
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                                VerticalDivider(
                                    modifier = Modifier.fillMaxHeight(),
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.inverseOnSurface
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 8.dp),
                                    //contentAlignment = Alignment.Center
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Slider(
                                        modifier = Modifier.fillMaxWidth(),
                                        state = SliderState(0.2f),
                                        colors = SliderDefaults.colors(inactiveTrackColor = MaterialTheme.colorScheme.surfaceBright)
                                    )
                                    Slider(
                                        modifier = Modifier.fillMaxWidth(),
                                        state = SliderState(0.8f),
                                        colors = SliderDefaults.colors(inactiveTrackColor = MaterialTheme.colorScheme.surfaceBright)
                                    )
                                    /*Slider(
                                        modifier = Modifier.fillMaxWidth(),
                                        state = SliderState(0.8f),
                                        colors = SliderDefaults.colors(inactiveTrackColor = MaterialTheme.colorScheme.surfaceBright)
                                    )*/
                                }
                            }
                        }
                    }

                    //HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    // Tuner
                    Text(
                        text = "   " + stringResource(id = R.string.tuner), //.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Italic
                    )

                    Card(
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                                //.padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // Active note indicator
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = "E#",
                                fontSize = 64.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                //fontStyle = FontStyle.Italic
                            )
                            // Row of tuning's notes
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ){
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontSize = 32.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("E")
                                        }
                                        withStyle(style = SpanStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("2")
                                        }
                                    }
                                )
/*                                Text(text = "E",
                                    fontSize = 32.sp,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontWeight = FontWeight.Bold
                                    //fontStyle = FontStyle.Italic
                                )*/
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontSize = 32.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("A")
                                        }
                                        withStyle(style = SpanStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("2")
                                        }
                                    }
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontSize = 32.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("D")
                                        }
                                        withStyle(style = SpanStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("3")
                                        }
                                    }
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontSize = 32.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("G")
                                        }
                                        withStyle(style = SpanStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("3")
                                        }
                                    }
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontSize = 32.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("B")
                                        }
                                        withStyle(style = SpanStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("3")
                                        }
                                    }
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontSize = 32.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("E")
                                        }
                                        withStyle(style = SpanStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                                            append("4")
                                        }
                                    }
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.inverseOnSurface) // was onSecondary

                            // Tuner buttons
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(MaterialTheme.colorScheme.secondary),
                                horizontalArrangement = Arrangement.SpaceAround,
                                //.sizeIn(minHeight = 48.dp)
                            ){
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .size(48.dp),
                                    contentAlignment = Alignment.Center
                                ){
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Default.ChevronLeft,
                                            contentDescription = "Favorite",
                                            tint = MaterialTheme.colorScheme.inverseOnSurface
                                        )
                                    }
                                }
                                VerticalDivider(
                                    modifier = Modifier.fillMaxHeight(),thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.inverseOnSurface
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f),//.fillMaxWidth(fraction = 0.6f),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        text = "Standart", // TODO - change for dynamic tuning name
                                        fontSize = 24.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        //fontStyle = FontStyle.Italic
                                    )
                                }
                                VerticalDivider(
                                    modifier = Modifier.fillMaxHeight(),thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.inverseOnSurface
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .size(48.dp),
                                    contentAlignment = Alignment.Center
                                ){
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = "Favorite",
                                            tint = MaterialTheme.colorScheme.inverseOnSurface
                                        )
                                    }
                                }
                            }


                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    // Input/Output devices
                    Text(
                        text = "   " + stringResource(id = R.string.input_device), //.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Italic
                    )

                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = {
                            expandedInput = !expandedInput
                        }
                    ) {
                        //OutlinedTextField(value = "x", onValueChange = {})
                        TextField(
                            //label = "Input",
                            value = "Input device",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedInput) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedInput,
                            onDismissRequest = { expandedInput = false }
                        ) {
                            inputOptions.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        selectedInput = item
                                        expandedInput = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }

                    }

                    Text(
                        text = "   " + stringResource(id = R.string.output_device), //.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Italic
                    )
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = {
                            expandedOutput = !expandedOutput
                        }
                    ) {
                        TextField( // TODO - add 1 line limit here
                            //label = "Output",
                            value = "Output device",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOutput) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedOutput,
                            onDismissRequest = { expandedOutput = false }
                        ) {
                            outputOptions.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        selectedOutput = item
                                        expandedOutput = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }

                    }




                }



            },
            measurePolicy = { measurables, constraints ->
                lateinit var headerMeasurable: Measurable
                lateinit var contentMeasurable: Measurable
                measurables.forEach {
                    when (it.layoutId) {
                        LayoutType.HEADER -> headerMeasurable = it
                        LayoutType.CONTENT -> contentMeasurable = it
                        else -> error("Unknown layoutId encountered!")
                    }
                }

                val headerPlaceable = headerMeasurable.measure(constraints)
                val contentPlaceable = contentMeasurable.measure(
                    constraints.offset(vertical = -headerPlaceable.height)
                )
                layout(constraints.maxWidth, constraints.maxHeight) {
                    // Place the header, this goes at the top
                    headerPlaceable.placeRelative(0, 0)

                    // Determine how much space is not taken up by the content
                    val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

                    val contentPlaceableY = when (navigationContentPosition) {
                        // Figure out the place we want to place the content, with respect to the
                        // parent (ignoring the header for now)
                        PedalboardNavigationContentPosition.TOP -> 0
                        PedalboardNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
                    }
                        // And finally, make sure we don't overlap with the header.
                        .coerceAtLeast(headerPlaceable.height)

                    contentPlaceable.placeRelative(0, contentPlaceableY)
                }
            }


        )
    }
}

/*fun SliderColors(inactiveTrackColor: Color): SliderColors {

}*/

enum class LayoutType {
    HEADER, CONTENT
}
