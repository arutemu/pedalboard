package com.mukuro.pedalboard.ui.components

// advice from ChatGPT for Volume Knob

// for background
// for palette
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import com.mukuro.pedalboard.R
import com.mukuro.pedalboard.data.Plugin
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap


/* TODO
*   1 - check comment below > card layout should be reworked fully
*       a - partially done. but there is an issue with Volume Knob
*           if it's size is too small, knob name is not visible.
*           need to understand why and fix it to be scalable
*   2 - found a way to get palette from images > get colors
*   3 - got colors? apply to knobs
*   4 - rename the fcking VOLUME KNOB to just KNOB*/

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun PedalboardPluginListItem(
    plugin: Plugin,
    navigateToDetail: (Long) -> Unit,
    toggleSelection: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isOpened: Boolean = false,
    isSelected: Boolean = false,
) {
    Card(
        modifier = modifier
            .width(360.dp)
            //.fillMaxHeight(0.95f)   // need to change it to constrain > found a way tp maintain aspect ratio
            .fillMaxHeight()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            //.aspectRatio() << may be useful
            .semantics { selected = isSelected }
            .clip(CardDefaults.shape)
/*            .combinedClickable( // Need to remove this
                onClick = { navigateToDetail(plugin.id) },
                onLongClick = { toggleSelection(plugin.id) }
            )*/
            .clip(CardDefaults.shape),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else if (isOpened) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

            val imageResource = getCardImageResource(plugin.name)

            // TODO - try to get palette from the image
            //val iconBitmap = (imageResource as ImageBitmap) // ERROR HERE > CRASHES THE APP
            //createPaletteAsync(imageResource)
            /*            val palette = rememberDominantColorPalette(iconBitmap)
                        val dominantColor = palette.dominantSwatch?.rgb as? Color ?: Color.White*/

            // BACKGROUND IMAGE
            if (imageResource != null) {

                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .scale(1.8f, 1.8f)
                )
            }

            // MAIN COLUMN - header + content
            Column(modifier = Modifier.fillMaxSize()) {
                // HEADER
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        //.padding(top = 12.dp)
                        .background(Color(0x30000000))
                ) {
                    Text(
                        text = plugin.name,
                        modifier = Modifier
                            .weight(1f)
                            //.align(Alignment.CenterHorizontally)
                            .padding(start = 12.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 30.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    IconButton( // Icon 1
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 4.dp)
                        //.align(Alignment.Vertical)
                        //.clip(CircleShape)
                        //.background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RadioButtonChecked,
                            contentDescription = "Move vertically",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton( // Icon 2
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 4.dp)
                        //.align(Alignment.Vertical)
                        //.clip(CircleShape)
                        //.background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton( // Icon 3
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 4.dp)
                        //.align(Alignment.Vertical)
                        //.clip(CircleShape)
                        //.background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                // CONTENT
                VolumeKnob(
                    modifier = Modifier.size(150.dp),
                    //knobSize = 200f,
                    knobName = "Volume 1",
                    knobColor = Color.Yellow,
                    indicatorColor = Color.Blue,
                    onValueChanged = { value ->
                        // Placeholder code for handling volume change
                        println("Volume changed: $value")
                    }
                )
                VolumeKnob(
                    modifier = Modifier.size(150.dp),
                    //knobSize = 200f,
                    knobName = "Gain 2",
                    knobColor = Color.LightGray,
                    indicatorColor = Color.DarkGray,
                    onValueChanged = { value ->
                        // Placeholder code for handling volume change
                        println("Volume changed: $value")
                    }
                )
/*                Text(
                    text = plugin.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )*/
            }
        }

    }
}

@Composable
fun VolumeKnob(
    modifier: Modifier = Modifier,
    knobSize: Float = 100f,
    knobName: String,
    knobColor: Color = Color.Gray,
    indicatorColor: Color = Color.Red,
    onValueChanged: (Float) -> Unit,
) {
    var angle by remember { mutableStateOf(0f) }

    Column(
        modifier = modifier.pointerInput(Unit) {
            detectDragGestures { change, _ ->
                val dragDistance = change.position - change.previousPosition
                angle += dragDistance.x / (8 * knobSize)
                angle = angle.coerceIn(0f, 1f)
                onValueChanged(angle * 2 - 1) // Map the angle to the range from -1 to 1
            }
        }
    ) {
        Text(
            text = (angle * 100).toInt().toString(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally) // would be good to add some outline here like in commented lines
                //.clip(CircleShape)
                .padding(top = 8.dp),
            style = TextStyle(color = Color.Black, fontSize = 12.sp)
        )

        Box(
            modifier = Modifier
                .size(knobSize.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
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
                //val indicatorRadius = radius - 20.dp.toPx()
                val indicatorRadius = radius * 0.2f
                val angleOffset = 60 // Offset the angle by 150 degrees to start at 7 and end at 5
                val angleInDegrees = (angle * 360).coerceIn(0f, 300f)
                val indicatorOffsetX = (radius - indicatorRadius) * cos(Math.toRadians(angleInDegrees.toDouble()-angleOffset.toDouble())).toFloat()
                val indicatorOffsetY = (radius - indicatorRadius) * sin(Math.toRadians(angleInDegrees.toDouble()-angleOffset.toDouble())).toFloat()
                val indicatorCenterX = centerX - indicatorOffsetX
                val indicatorCenterY = centerY - indicatorOffsetY // Invert the Y-axis to start from the top

                drawCircle(
                    color = indicatorColor,
                    center = Offset(indicatorCenterX, indicatorCenterY),
                    radius = indicatorRadius
                )
            }
        }
        Text(
            text = knobName,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 0.dp),
            style = TextStyle(color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        )
    }
}

fun getCardImageResource(stringData: String): Int? {
    // Create a mapping between the string values and the corresponding image resources
    return when (stringData) {
        "Nayuta" -> R.drawable.nayuta
        "Makima" -> R.drawable.makima
        "Reze" -> R.drawable.reze
        "Power" -> R.drawable.power
        else -> null
    }
}

// some shitty code for palettes
fun createPaletteAsync(bitmap: Bitmap) {
    Palette.from(bitmap).generate { palette ->
        // Use the generated instance
    }
}

@Composable
fun rememberDominantColorPalette(image: ImageBitmap): Palette {
    return remember(image) {
        val bitmap = image.asAndroidBitmap()
        Palette.Builder(bitmap).generate()
    }
}
