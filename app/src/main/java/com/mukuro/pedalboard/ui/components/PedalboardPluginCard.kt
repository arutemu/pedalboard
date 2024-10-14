package com.mukuro.pedalboard.ui.components

// advice from ChatGPT for Volume Knob

// for background
// for palette
import android.graphics.BitmapFactory
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.DefaultStrokeLineJoin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukuro.pedalboard.data.Plugin
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.mukuro.pedalboard.R
import com.mukuro.pedalboard.data.Knob
import com.mukuro.pedalboard.ui.components.plugin.PluginKnob
import com.mukuro.pedalboard.data.RangeSlider
import com.mukuro.pedalboard.data.Slider
import com.mukuro.pedalboard.data.Switch
import com.mukuro.pedalboard.data.local.LocalPluginsDataProvider
import com.mukuro.pedalboard.ui.components.plugin.NeuroPluginKnob
import com.mukuro.pedalboard.ui.components.plugin.PluginSlider

/**
 * TODO
 * -
* */

/** Basically, the Card element for all the plugins on the rack.
 * As for now, only this one is needed.
 * @param plugin is self-descriptive, others are as well lol
 *
 * @param navigateToDetail
 * @param toggleSelection
 * @param isOpened and
 * @param isSelected are leftovers from Reply app example
 * Leaving them atm. in case any proper user-case is defined.
 * */
@OptIn(
    ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)
@Composable
fun PedalboardPluginCard(
    plugin: Plugin,
    //navigateToDetail: (Long) -> Unit,
    //toggleSelection: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isOpened: Boolean = false,
    isSelected: Boolean = false,
) {

    // Calculate Plugin Card values
    var maxItemsCount = 3
    when (plugin.aspectRatio) {
        in 0f..0.7f-> maxItemsCount = 3
        in 0.65f..1f -> maxItemsCount = 4
        in 1f..Float.MAX_VALUE -> maxItemsCount = 5
     else -> Arrangement.SpaceAround
    }
    // Default colors
    val colorArray : Array<Color> = arrayOf(
        Color.DarkGray,
        Color.LightGray,
        Color.Red
    )
    val palette : Palette


    // Get colours palette
    val context = LocalContext.current
    /* Convert our Image Resource into a Bitmap */
/*    plugin.coverDrawable?.let {
        val bitmap = remember {
            BitmapFactory.decodeResource(context.resources, plugin.coverDrawable)
        }
        *//* Create the Palette, pass the bitmap to it *//*
        palette = remember {
            Palette.from(bitmap).generate()
        }
*//*        *//**//* Get the dark vibrant swatch *//**//*
        val vibrantSwatch = palette.vibrantSwatch
        val darkVibrantSwatch = palette.darkVibrantSwatch
        val darkMutedSwatch = palette.darkMutedSwatch
        *//**//* Save array of colours *//**//*
        //colorArray[0] = Color(palette.vibrantSwatch!!.rgb)
        colorArray[0] = vibrantSwatch?.let { Color(it.rgb) } ?: Color.DarkGray
        colorArray[1] = darkVibrantSwatch?.let { Color(it.rgb) } ?: Color.LightGray
        colorArray[2] = darkMutedSwatch?.let { Color(it.rgb) } ?: Color.Red*//*

        colorArray[0] = palette.vibrantSwatch?.let { Color(it.rgb) } ?: Color.DarkGray
        colorArray[1] = palette.darkVibrantSwatch?.let { Color(it.rgb) } ?: Color.LightGray
        colorArray[2] = palette.darkMutedSwatch?.let { Color(it.rgb) } ?: Color.Red
    }*/








    Card(
        modifier = modifier
            .aspectRatio(plugin.aspectRatio) //<< may be useful, ALSO can put it after fillMaxHeight() for different result
            .fillMaxHeight()
            //.width(360.dp) // REMOVE or refactor if needed
            //.padding(start = 12.dp, end = 12.dp)
            .padding(vertical = 20.dp)
            .semantics { selected = isSelected } // wtf
            .clip(CardDefaults.shape)
            /*            .combinedClickable( // Need to remove this
                onClick = { navigateToDetail(plugin.id) },
                onLongClick = { toggleSelection(plugin.id) }
            )*/
            .clip(CardDefaults.shape),
            //.safeDrawingPadding(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else if (isOpened) MaterialTheme.colorScheme.secondaryContainer // WTF
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation( // Elevation is not visible here Y__Y Probably covered by top elements
            defaultElevation = 6.dp
        )
    ) {
        var turnedOn: Boolean by rememberSaveable { mutableStateOf(true) } // TODO - change to rememberSavable if needed

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant,
                        )
                    ),
                    shape = RectangleShape
                )
            // Possible draw_behind implementation
/*                .drawBehind {
                    drawIntoCanvas { canvas ->
                        drawable?.let {
                            it.setBounds(0, 0, size.width.roundToInt(), size.height.roundToInt())
                            it.draw(canvas.nativeCanvas)
                        }
                    }
                }*/
        )
        { // BoxWithConstraints

            // Native Image element (slow and laggy on scrolling)
/*            plugin.coverDrawable?.let {
                Image( // from Coil's AsyncImage
                    painter = painterResource(id = plugin.coverDrawable),
                    contentDescription = "cover image",
                    contentScale = ContentScale.Crop,
                    alignment = BiasAlignment(0f, -1f),
                    modifier = Modifier
                        .fillMaxSize()
*//*                        .blur(
                            radiusX = 2.dp,
                            radiusY = 2.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )*//*
                        .clip(RoundedCornerShape(8.dp))
                )
            }*/

            // Coil Image element
            // TODO - disabled for TESTING PURPOSES till line 258
/*            plugin.coverDrawable?.let {
                // Draw image
                AsyncImage(
                    model = plugin.coverDrawable,
                    contentDescription = "cover",
                    contentScale = ContentScale.Crop,
                    alignment = BiasAlignment(0f, -1f),
                    modifier = Modifier
                        .fillMaxSize()
                )
                // TODO - reenable?
                // Generate palette
                val bitmap = remember {
                    BitmapFactory.decodeResource(
                        context.resources,
                        plugin.coverDrawable
                    )
                }
            }*/

            /*

                *//* Create the Palette, pass the bitmap to it *//**//*
                //val palette = remember { Palette.from(bitmap).generate() }
                val palette = remember {
                    Palette.from(bitmap).generate()
                }
                
                colorArray[0] = palette.vibrantSwatch?.let { Color(it.rgb) } ?: Color.DarkGray
                colorArray[1] = palette.darkVibrantSwatch?.let { Color(it.rgb) } ?: Color.LightGray
                colorArray[2] = palette.darkMutedSwatch?.let { Color(it.rgb) } ?: Color.Red
            }*//*
            }*/

            // TODO - part of background image implementation. Waiting for a proper refactor
            //val imageResource = getCardImageResource(plugin.name)
            // TODO - try to get palette from the image
            //val iconBitmap = (imageResource as ImageBitmap) // ERROR HERE > CRASHES THE APP
            //createPaletteAsync(imageResource)
            /*            val palette = rememberDominantColorPalette(iconBitmap)
                        val dominantColor = palette.dominantSwatch?.rgb as? Color ?: Color.White*/
            /*TODO -
                BACKGROUND IMAGE implementation is here. However, currently it's anything but smooth
                So commented for now until a proper refactor.
             */
/*            if (imageResource != null) {

                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .scale(1.8f, 1.8f)
                )
            }*/

            /** MAIN plugin column = header + content
             * */
            Column(modifier = Modifier
                .fillMaxSize()
                //.background(MaterialTheme.colorScheme.background)
            ) {
                // HEADER
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        //.padding(top = 12.dp)
                        //.safeContentPadding()
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                        //.alpha(50f)
                ) {
                    Text(
                        text = plugin.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .basicMarquee() // Experimental shit
                            .weight(1f)
                            //.align(Alignment.CenterHorizontally)
                            .padding(start = 12.dp, end = 4.dp),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Normal,
                            //fontFamily = FontFamily.Cursive,
                            //fontWeight = FontWeight.Bold,
                        )
                    )
                    IconToggleButton( // Icon 1
                        checked = turnedOn,
                        onCheckedChange = { turnedOn = it },
                        modifier = Modifier
                            //.aspectRatio(1f)
                            .size(40.dp)
                        //.align(Alignment.Vertical)
                        //.clip(CircleShape)
                        //.background(MaterialTheme.colorScheme.surface)
                    ) {
                        val tint by animateColorAsState(if (turnedOn) Color(0xFF5EC281) else Color(0xffc25e66), label = "ON State")
                        Icon(
                            imageVector = Icons.Default.RadioButtonChecked,
                            contentDescription = "Move vertically",
                            tint = tint
                        )
                    }
                    //Spacer(modifier = Modifier.width(4.dp))
                    IconButton( // Icon 2
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            //.aspectRatio(1f)
                            .size(40.dp)
                        //.align(Alignment.Vertical)
                        //.clip(CircleShape)
                        //.background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                    //Spacer(modifier = Modifier.width(4.dp))
                    IconButton( // Icon 3
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            //.aspectRatio(1f)
                            .size(40.dp)
                            //.padding(end = 4.dp)
                        //.align(Alignment.Vertical)
                        //.clip(CircleShape)
                        //.background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }

                /** FlowRow is experimental in Compose, just in case!
                 * Here lies the content of each pedals - Knobs (and other elements in the future).
                 * */
                FlowRow(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .fillMaxSize()
                        .padding(4.dp), // 4-8 is ok
                        //.align(Alignment.CenterVertically), // TODO - why isn't this working? MUST MORE BLOOD BE SHED?!11
                    horizontalArrangement = plugin.horizontal ?: Arrangement.Center,
                    //verticalArrangement = plugin.vertical ?: Arrangement.Top, //Arrangement.Center, //Arrangement.Absolute.SpaceEvenly, //.spacedBy(4.dp),
                    maxItemsInEachRow = maxItemsCount //3
                )
                {
                    /** New version of content for Plugin list.
                        TODO
                          1 finalize knob (hehe)
                            1.a at least finalize size + font + knob values (not hehe...)
                          2 remove old part below + rename funcs
                          3 check performance
                          4 ...
                     */
                    plugin.elements?.forEach {
                        when (it) {
                            is Knob -> NeuroPluginKnob(
                                it,
                                knobSize = 80f, //100f - (it.id - 1f) * 10f, // < -- Stuff to check dynamic sizes
                                onValueChanged = { value ->
                                    /* Placeholder code for handling volume change
                                    * Put here some action if needed in the future.
                                    * Right now only prints logs on change.
                                    */
                                    println(it.name+" changed: $value")},
                                value = 1f,
                                //modifier = Modifier.size(height = 150.dp, width = 120.dp), // TODO - wtf, remove this!!
                                colors = colorArray// Let's settle for this size for now .____.
                                )
                            is Switch -> {}
                            is Slider -> { PluginSlider(
                                it,
                                onValueChanged = { value ->
                                    /* Placeholder code for handling volume change
                                    * Put here some action if needed in the future.
                                    * Right now only prints logs on change.
                                    */
                                    println(it.name+" changed: $value")},
                                value = 1f,
                                //colors = colorArray
                            )
                            }
                            is RangeSlider -> {}

                            // TODO - add final elements
                            // is Selector -> {}
                            // is XXX -> {}

                        }
                    }

                }
            }
        }
    }
}




/*fun getCardImageResource(stringData: String): Int? {
    // Create a mapping between the string values and the corresponding image resources
    return when (stringData) {
        "Nayuta" -> R.drawable.nayuta
        "Makima" -> R.drawable.makima
        "Reze" -> R.drawable.reze
        "Power" -> R.drawable.power
        else -> null
    }
}*/
@PreviewLightDark
@Preview(showBackground = false, heightDp = 600)
@Composable
fun PedalboardTabletCardPreview() {
    PedalboardPluginCard(
        plugin = LocalPluginsDataProvider.allPlugins[1],
        isOpened = false, //openedPlugin?.id == plugin.id,
        isSelected = false //selectedPluginIds.contains(plugin.id)
    )
}

@PreviewLightDark
@Preview(showBackground = false, widthDp = 400, heightDp = 700)
@Composable
fun PedalboardMobileCardPreview() {
    PedalboardPluginCard(
        plugin = LocalPluginsDataProvider.allPlugins[1],
        isOpened = false, //openedPlugin?.id == plugin.id,
        isSelected = false //selectedPluginIds.contains(plugin.id)
    )
}

/*@Preview(showBackground = false, widthDp = 200, heightDp = 200)
@Composable
fun PedalboardSmallKnobPreview() {
    PluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier.size(200.dp),
        knobSize = 200f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        }
    )
}

@Preview(showBackground = false, widthDp = 400, heightDp = 400)
@Composable
fun PedalboardMediumKnobPreview() {
    PluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[1],
        modifier = Modifier.size(400.dp),
        knobSize = 400f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        }
    )
}

@Preview(showBackground = false, widthDp = 600, heightDp = 600)
@Composable
fun PedalboardBigKnobPreview() {
    PluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier.size(600.dp),
        knobSize = 600f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        }
    )
}*/



// some shitty code for palettes
/*fun createPaletteAsync(bitmap: Bitmap) {
    Palette.from(bitmap).generate { palette ->
        // Use the generated instance
    }
}*/

/*@Composable
fun rememberDominantColorPalette(image: ImageBitmap): Palette {
    return remember(image) {
        val bitmap = image.asAndroidBitmap()
        Palette.Builder(bitmap).generate()
    }
}*/
