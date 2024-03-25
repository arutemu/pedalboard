package com.mukuro.pedalboard.ui.components

// advice from ChatGPT for Volume Knob

// for background
// for palette
import android.graphics.BitmapFactory
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukuro.pedalboard.data.Plugin
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.tooling.preview.Preview
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.mukuro.pedalboard.R
import com.mukuro.pedalboard.data.Knob
import com.mukuro.pedalboard.data.RangeSlider
import com.mukuro.pedalboard.data.Slider
import com.mukuro.pedalboard.data.Switch
import com.mukuro.pedalboard.data.local.LocalPluginsDataProvider

/* TODO
*   1 - check comment below > card layout should be reworked fully
*       a - partially done. but there is an issue with Volume Knob
*           if it's size is too small, knob name is not visible.
*           need to understand why and fix it to be scalable
*   2 - found a way to get palette from images > get colors
*   3 - got colors? apply to knobs
*   4 - rename the fcking VOLUME KNOB to just KNOB*/

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
    ExperimentalLayoutApi::class
)
@Composable
fun PedalboardPluginCard(
    plugin: Plugin,
    navigateToDetail: (Long) -> Unit,
    toggleSelection: (Long) -> Unit,
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


    // Get colours palette
    val context = LocalContext.current

    /* Convert our Image Resource into a Bitmap */
    plugin.coverDrawable?.let {
        val bitmap = remember {
            BitmapFactory.decodeResource(context.resources, plugin.coverDrawable)
        }
        /* Create the Palette, pass the bitmap to it */
        val palette = remember {
            Palette.from(bitmap).generate()
        }
        /* Get the dark vibrant swatch */
        val darkVibrantSwatch = palette.darkVibrantSwatch
         /* Save array of colours */
        val colorArray : Array<Int> = arrayOf(
            palette.vibrantSwatch!!.rgb,
            palette.darkVibrantSwatch!!.rgb,
            palette.lightMutedSwatch!!.rgb,
            palette.mutedSwatch!!.rgb,
            palette.darkMutedSwatch!!.rgb
        )
    }






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
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else if (isOpened) MaterialTheme.colorScheme.secondaryContainer // WTF
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        var turnedOn: Boolean by rememberSaveable { mutableStateOf(true) } // TODO - change to rememberSavable if needed

        // Experimental image corner
/*        val uri = Uri.parse("android.resource://com.mukuro.pedalboard/" + "R.drawable." + plugin.name.lowercase().replace(" ", "_"))
        val shortUri = Uri.parse("R.drawable." + plugin.name.lowercase().replace(" ", "_"))
        // val resId: Int = LocalContext.current.resources.getIdentifier("makima", "drawable", )
        val drawable = LocalContext.current.getDrawable(R.drawable.makima)*/

        Box(
            modifier = Modifier
                .fillMaxSize()
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
            plugin.coverDrawable?.let {
                AsyncImage(
                    model = plugin.coverDrawable,
                    contentDescription = "Makima",
                    contentScale = ContentScale.Crop,
                    alignment = BiasAlignment(0f, -1f),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

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
                        .height(48.dp)
                        .fillMaxWidth()
                        //.padding(top = 12.dp)
                        .safeContentPadding()
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                        //.alpha(50f)
                ) {
                    Text(
                        text = plugin.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            //.align(Alignment.CenterHorizontally)
                            .padding(start = 12.dp, end = 4.dp),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Normal,
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
                        val tint by animateColorAsState(if (turnedOn) Color(0xff8cff78) else Color(0xFFEC407A), label = "ON State")
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
                    horizontalArrangement = plugin.horizontal ?: Arrangement.Center,
                    verticalArrangement = plugin.vertical ?: Arrangement.Top, //Arrangement.Center, //Arrangement.Absolute.SpaceEvenly, //.spacedBy(4.dp),
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
                            is Knob -> PluginKnob(
                                it,
                                knobSize = 100f,
                                onValueChanged = { value ->
                                    /* Placeholder code for handling volume change
                                    * Put here some action if needed in the future.
                                    * Right now only prints logs on change.
                                    */
                                    println(it.name+" changed: $value")},
                                value = 1f,
                                modifier = Modifier.size(height = 150.dp, width = 120.dp) // Let's settle for this size for now .____.
                                )
                            is Switch -> {}
                            is Slider -> {}
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

/** Small lerp function, cuz I found no easier way to map value from (0..1) to (min..max)
 *   WTF, there is no built-in lerp function? or am I stupid? (not hehe)
 *   TODO - refactor, maybe this additional func is overkill
 */
fun Float.mapRange(
    fromMin: Float,
    fromMax: Float,
    toMin: Float,
    toMax: Float
): Float {
    if (fromMin == fromMax) {
        throw IllegalArgumentException("Input range cannot have equal min and max values")
    }

    val clampedValue = this.coerceIn(fromMin, fromMax)
    return (clampedValue - fromMin) * (toMax - toMin) / (fromMax - fromMin) + toMin
}

@Composable
fun PluginKnob(
    knob: Knob,
    modifier: Modifier = Modifier,
    knobSize: Float = 100f,
    knobColor: Color = Color.DarkGray,
    indicatorColor: Color = Color.LightGray,
    onValueChanged: (Float) -> Unit,
    value: Float = 0f,
    colors: Array<Int>? = null
    //content: @Composable () -> Unit // TODO - probably remove, don't think it may be needed here in the future
) {
    val startPosition : Float = (value * (knob.endPoint - knob.startPoint) / 360) // TODO fix calculation
    var angle: Float by rememberSaveable { mutableStateOf(startPosition) }

    Column(
        modifier = modifier
            .wrapContentSize()
            .pointerInput(Unit) { // Probably a good idea is to change the gesture to .draggable
                detectDragGestures { change, _ ->
                    val dragDistance = change.position - change.previousPosition
                    angle += dragDistance.x / (8 * knobSize)
                    angle = angle.coerceIn(0f, 1f)
                    onValueChanged((angle * 2) - 1) // Map the angle to the range from -1 to 1
                }
            }
    ) {
        Text( // Current knob value
            text = "%.1f".format(angle.mapRange(0f,1f, knob.startPoint, knob.endPoint)).toFloat().toString()+knob.measure,
            modifier = Modifier
                .align(Alignment.CenterHorizontally) // would be good to add some outline here like in commented lines
                //.clip(CircleShape)
                .padding(top = 4.dp, bottom = 4.dp),
            //.wrapContentSize(Alignment.TopCenter, unbounded = false),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
            //, fontSize = 12.sp)
        )
        Box(
            modifier = Modifier
                //.fillMaxWidth(fraction = 0.8f)
                .align(Alignment.CenterHorizontally)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize(fraction = 0.6f)
                    .aspectRatio(1f)// sizeIn(minWidth = 100.dp, minHeight = 100.dp, maxWidth = 120.dp, maxHeight = 120.dp) //.fillMaxSize() //fraction = 0.8f)
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = size.minDimension / 2 - 8.dp.toPx()

                // Draw the filled knob circle
                drawCircle(
                    color = knobColor,
                    center = Offset(centerX, centerY),
                    radius = radius
                )

                // Draw the indicator circle
                // THIS IS STUPID SHIT (but somehow it's working... at least partially)
                val indicatorRadius = radius * 0.2f
                val angleOffset = 60 // Offset the angle by 150 degrees to start at 7 and end at 5
                val angleInDegrees = (angle * 360) / 1.2f //.coerceIn(0f, 300f) // SOMETHING FISHY HERE // MY CODE // FIXED EHHEHEHE :3
                val indicatorOffsetX = (radius - indicatorRadius) * cos(Math.toRadians(angleInDegrees.toDouble()-angleOffset.toDouble())).toFloat()
                val indicatorOffsetY = (radius - indicatorRadius) * sin(Math.toRadians(angleInDegrees.toDouble()-angleOffset.toDouble())).toFloat()
                val indicatorCenterX = centerX - indicatorOffsetX
                val indicatorCenterY = centerY - indicatorOffsetY // Invert the Y-axis to start from the top

                val pointerLeftX = centerX + (cos(Math.toRadians(254.0)).toFloat() * (radius * 1.28f))
                val pointerY = centerY - (sin(Math.toRadians(254.0)).toFloat() * (radius * 1.28f))
                val pointerRightX = centerX - (cos(Math.toRadians(254.0)).toFloat() * (radius * 1.28f))

                val colorStops = arrayOf(
                    0.0f to Color.DarkGray,
                    0.16f to Color.Red,
                    0.33f to Color.LightGray,
                    0.75f to Color.DarkGray
                )
                // Left marker
                drawCircle(
                    color = Color.LightGray,
                    center = Offset(pointerLeftX,pointerY),
                    radius = 4f
                )
                // Right marker
                drawCircle(
                    color = Color.LightGray,
                    center = Offset(pointerRightX,pointerY),
                    radius = 4f
                )
                // Arc indicator around the Knob
                drawArc(
                    brush = Brush.sweepGradient(colorStops = colorStops), // listOf(Color.LightGray, Color.Magenta, Color.Red),
                    startAngle = 120f,
                    sweepAngle = angleInDegrees,
                    useCenter = false,
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )
                // Indicator on the Knob
                drawCircle(
                    color = indicatorColor,
                    center = Offset(indicatorCenterX, indicatorCenterY),
                    radius = indicatorRadius * 0.5f
                )
            }
        }
        Text(
            text = knob.name,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp, bottom = 4.dp),
            //style = TextStyle(color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
@Composable
fun PluginSlider(
    slider: Slider,
    modifier: Modifier = Modifier,
    sliderColor: Color = Color.DarkGray,
    indicatorColor: Color = Color.LightGray,
    onValueChanged: (Float) -> Unit,
    value: Float = 0f
    //content: @Composable () -> Unit // TODO - probably remove, don't think it may be needed here in the future
) {
    val startPosition : Float = (value * (slider.endValue - slider.startValue) / 360) // TODO fix calculation
    var point: Float by rememberSaveable { mutableStateOf(startPosition) }


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

@Preview(showBackground = false, heightDp = 600)
@Composable
fun PedalboardTabletCardPreview() {
    PedalboardPluginCard(
        plugin = LocalPluginsDataProvider.allPlugins[1],
        navigateToDetail = { /*pluginId ->
            navigateToDetail(pluginId, PedalboardContentType.SINGLE_PANE)*/
        },
        toggleSelection = {}, //togglePluginSelection,
        isOpened = false, //openedPlugin?.id == plugin.id,
        isSelected = false //selectedPluginIds.contains(plugin.id)
    )
}

@Preview(showBackground = false, widthDp = 400, heightDp = 600)
@Composable
fun PedalboardMobileCardPreview() {
    PedalboardPluginCard(
        plugin = LocalPluginsDataProvider.allPlugins[1],
        navigateToDetail = { /*pluginId ->
            navigateToDetail(pluginId, PedalboardContentType.SINGLE_PANE)*/
        },
        toggleSelection = {}, //togglePluginSelection,
        isOpened = false, //openedPlugin?.id == plugin.id,
        isSelected = false //selectedPluginIds.contains(plugin.id)
    )
}

@Preview(showBackground = false, widthDp = 200, heightDp = 200)
@Composable
fun PedalboardSmallKnobPreview() {
    PluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[3],
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
}



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
