package com.mukuro.pedalboard.ui.components.plugin

import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukuro.pedalboard.data.Knob
import com.mukuro.pedalboard.data.local.LocalPluginsDataProvider
import kotlin.math.cos
import kotlin.math.sin

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
    knobSize: Float, // = 100f,
    knobColor: Color = Color.DarkGray,
    indicatorColor: Color = Color.LightGray,
    onValueChanged: (Float) -> Unit,
    value: Float = 0f,
    colors: Array<Color>? = null
    //content: @Composable () -> Unit // TODO - probably remove, don't think it may be needed here in the future
) {
    val startPosition : Float = (value * (knob.endPoint - knob.startPoint) / 360) // TODO fix calculation
    var angle: Float by rememberSaveable { mutableStateOf(startPosition) }

    Column(
        modifier = modifier
            .requiredSize(knobSize.dp, knobSize.dp + 24.dp)
            //.requiredWidth(knobSize.dp)
            //.requiredHeight(knobSize.dp)
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
            color = colors?.get(0) ?: Color.DarkGray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.45f)
                // would be good to add some outline here like in commented lines
                //.clip(CircleShape)
                .padding(top = 4.dp, bottom = 4.dp)
                //.alpha(0.5f)
                .background(color = (colors?.get(1) ?: knobColor), shape = RoundedCornerShape(size = 12.dp))
                .align(Alignment.CenterHorizontally),
            //.wrapContentSize(Alignment.TopCenter, unbounded = false),
            maxLines = 1,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium //FontWeight.Medium
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
                    color = colors?.get(1) ?: knobColor, //?: knobColor,
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

                /** Dot marker offset
                 *
                 * Previous formula was
                 *      centerX + (cos(Math.toRadians(254.0)).toFloat() * (radius * 1.28f))
                 * but I try to use some dynamic distance instead of a const 1.28f
                 *
                 * So let's try using fraction of KnobSize
                 */
                val pointerLeftX = centerX + (cos(Math.toRadians(254.0)).toFloat() * (radius * 1.28f))
                val pointerY = centerY - (sin(Math.toRadians(254.0)).toFloat() * (radius * 1.28f))
                val pointerRightX = centerX - (cos(Math.toRadians(254.0)).toFloat() * (radius * 1.28f))

                val colorStops = arrayOf(
                    0.0f to Color.LightGray, //DarkGray,
                    0.16f to Color.Red,
                    0.33f to Color.LightGray,
                    0.75f to Color.LightGray //DarkGray
                )
                // Left marker
                drawCircle(
                    color = colors?.get(1) ?: Color.LightGray,
                    center = Offset(pointerLeftX,pointerY),
                    radius = 4f
                )
                // Right marker
                drawCircle(
                    color = colors?.get(1) ?: Color.LightGray,
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
                    color = colors?.get(0) ?: indicatorColor,
                    center = Offset(indicatorCenterX, indicatorCenterY),
                    radius = indicatorRadius * 0.5f
                )
            }
        }
        Text(
            text = knob.name,
            color = colors?.get(0) ?: Color.DarkGray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.5f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp, bottom = 4.dp)
                .background(color = (colors?.get(1) ?: knobColor), shape = RoundedCornerShape(size = 30.dp)),
            //style = TextStyle(color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold),
            style = MaterialTheme.typography.labelMedium //titleMedium //.merge(
            /* TextStyle(color = colors?.get(1) ?: Color.DarkGray,
                //fontSize = 80.sp,
                drawStyle = Stroke(
                    miter = 10f,
                    width = 2f,
                    join = StrokeJoin.Round
                )
            )
        )*/,
            maxLines = 1,
            //fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun RadialPluginKnob(
    knob: Knob,
    modifier: Modifier = Modifier,
    knobSize: Float, // = 100f,
    knobColor: Color = Color.DarkGray,
    indicatorColor: Color = Color.LightGray,
    onValueChanged: (Float) -> Unit,
    value: Float = 0f,
    colors: Array<Color>? = null
    //content: @Composable () -> Unit // TODO - probably remove, don't think it may be needed here in the future
) {
    val startPosition : Float = (value * (knob.endPoint - knob.startPoint) / 360) // TODO fix calculation
    var angle: Float by rememberSaveable { mutableStateOf(startPosition) }

    // Creating a canvas with various attributes

     Column(
            modifier = modifier
                .requiredWidth(knobSize.dp)
                .requiredHeight(knobSize.dp)
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
                color = colors?.get(0) ?: Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.45f)
                    // would be good to add some outline here like in commented lines
                    //.clip(CircleShape)
                    .padding(top = 4.dp, bottom = 4.dp)
                    //.alpha(0.5f)
                    .background(color = (colors?.get(1) ?: knobColor), shape = RoundedCornerShape(size = 12.dp))
                    .align(Alignment.CenterHorizontally),
                //.wrapContentSize(Alignment.TopCenter, unbounded = false),
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium //FontWeight.Medium
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

                    // CURVED TEXT
                    drawIntoCanvas {
                        val textPadding = 0.dp.toPx()
                        val arcHeight = knobSize.dp.toPx()
                        val arcWidth = knobSize.dp.toPx()

                        // Path for curved text
                        val path = Path().apply {
                            addArc(0f, textPadding, arcWidth/2, arcHeight/2, 180f, 180f)
                        }

                        it.nativeCanvas.drawTextOnPath(
                            "Knob name!!",
                            path,
                            0f - knobSize,
                            0f,
                            Paint().apply {
                                textSize = 10.sp.toPx()
                                textAlign = Paint.Align.CENTER

                            },

                        )
                    }
                    
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val radius = size.minDimension / 2 - 8.dp.toPx()

                    // Draw the filled knob circle
                    drawCircle(
                        color = colors?.get(1) ?: knobColor, //?: knobColor,
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
                        0.0f to Color.LightGray, //DarkGray,
                        0.16f to Color.Red,
                        0.33f to Color.LightGray,
                        0.75f to Color.LightGray //DarkGray
                    )
                    // Left marker
                    drawCircle(
                        color = colors?.get(1) ?: Color.LightGray,
                        center = Offset(pointerLeftX,pointerY),
                        radius = 4f
                    )
                    // Right marker
                    drawCircle(
                        color = colors?.get(1) ?: Color.LightGray,
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
                        color = colors?.get(0) ?: indicatorColor,
                        center = Offset(indicatorCenterX, indicatorCenterY),
                        radius = indicatorRadius * 0.5f
                    )
                }
            }
            Text(
                text = knob.name,
                color = colors?.get(0) ?: Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.5f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp, bottom = 4.dp)
                    .background(color = (colors?.get(1) ?: knobColor), shape = RoundedCornerShape(size = 30.dp)),
                //style = TextStyle(color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold),
                style = MaterialTheme.typography.labelMedium //titleMedium //.merge(
                /* TextStyle(color = colors?.get(1) ?: Color.DarkGray,
                //fontSize = 80.sp,
                drawStyle = Stroke(
                    miter = 10f,
                    width = 2f,
                    join = StrokeJoin.Round
                )
            )
        )*/,
            maxLines = 1,
            //fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun NeuroPluginKnob(
    knob: Knob,
    modifier: Modifier = Modifier,
    knobSize: Float, // = 100f,
    knobColor: Color = Color.DarkGray,
    indicatorColor: Color = Color.LightGray,
    onValueChanged: (Float) -> Unit,
    value: Float = 0f,
    colors: Array<Color>? = null // TODO - refactor, need better way to pass color data
    //content: @Composable () -> Unit // TODO - probably remove, don't think it may be needed here in the future
) {

    /** 
     * DONE!
     * But still TODO
     *
     * Things to polish/improve:
     * 0) Rename the knob to be the main one, probably?
     * 1) implement color initialisation
     *     - some basics are already here
     *     - at the moment arc color is static and does not react to changing theme (light/dark)
     *     - as well as knob indicator
     * 2) probably remove padding OR set proper size value.
     *     - right now size of the element depends on the knobSize, but the UI element itself is wider
     *     - probably better to use the card Flow element spacing instead of padding
     * 3) Re-enable shadows/highlights where needed, it's code is commented ATM
     * 4) Add constrains for the min/max knob size value?
     * 5) Find out magic offset for labels canvas, lol
     * 6) And clean up all the mess, of course :D
     * */
    val labelColor = MaterialTheme.colorScheme.onSurface
    val secondaryColor = MaterialTheme.colorScheme.onSurface

    // Values for Knob functionality
    val startPosition : Float = (value * (knob.endPoint - knob.startPoint) / 100) // TODO fix calculation
    var angle: Float by rememberSaveable { mutableFloatStateOf(startPosition) }

    //val gray100 = Color(0xffe5e5e5)
    //val gray200 = Color(0xffd0d0d0)

    // Values for Knob handles
    val handleRadiusOffset = knobSize / 2f * 1.24f // TODO - find proper coeff
    val handleSize = knobSize.div(8) // TODO - find proper coeff
    val handleX = handleRadiusOffset * cos(Math.toRadians(240.0)).toFloat()
    val handleY = handleRadiusOffset * sin(Math.toRadians(240.0)).toFloat()

    var angleInDegrees = (angle * 360) / 1.2f

    // Values for Knob indicator
    val indicatorRadiusOffset = knobSize / 2f * 0.74f // TODO - find proper coeff
    val indicatorSize = knobSize.div(8) // TODO - find proper coeff
    val indicatorX = indicatorRadiusOffset * cos(Math.toRadians(120f + angleInDegrees.toDouble())).toFloat()
    val indicatorY = indicatorRadiusOffset * sin(Math.toRadians(120f + angleInDegrees.toDouble())).toFloat()


    Box( // Main Knob CONTAINER
        Modifier
            //.weight(1f)
            //.requiredSize(100.dp,100.dp)
            .padding(18.dp)
            .requiredSize(knobSize.dp * 1.24f,knobSize.dp * 1.24f) // TODO - recalculate size

            //
            //.wrapContentSize()
            //.weight(1f)
/*            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                                )
                            ),
                    shape = CircleShape
                )*/
            //.fillMaxSize()
/*            .border(
                width = 1.dp,
                shape = RectangleShape,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = .25f),
                        Color.Black.copy(alpha = .25f),
                    )
                )
            )*/,
        contentAlignment = Alignment.Center
    ) {
        // Main Knob circle
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)//fraction = 0.8f)
                .requiredSize(knobSize.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = knobSize.div(20f).dp,//6.dp,
                    shape = CircleShape,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = .25f),
                            Color.Black.copy(alpha = .25f),
                        )
                    )
                )
                .pointerInput(Unit) { // Probably a good idea is to change the gesture to .draggable? Not sure
                    detectDragGestures { change, _ ->
                        val dragDistance = change.position - change.previousPosition
                        angle += dragDistance.x / (8 * knobSize)
                        angle = angle.coerceIn(0f, 1f)
                        angleInDegrees = (angle * 360) / 1.2f
                        onValueChanged((angle * 2) - 1) // Map the angle to the range from -1 to 1
                    }
                }
        ) {

            // Knob Highlight
/*            Box(
                Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, -20) }
                    .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(Color.White.copy(alpha = .05f), CircleShape)
            )*/
            // Knob Shadow
            Box(
                Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, 20) }
                    .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(Color.Black.copy(alpha = .05f), CircleShape)
            )

            /** Indicator */
            Box( // Indicator
                Modifier // WTF IS THIS
                    .offset(indicatorX.dp, indicatorY.dp)
                    //.offset((indicatorRadiusOffset.times(cos(Math.toRadians(angleInDegrees.toDouble()))).toFloat()).dp, (indicatorRadiusOffset.times(sin(Math.toRadians(angleInDegrees.toDouble()))).toFloat()).dp)
                    .align(Alignment.Center)
                    .size(handleSize.dp)
                    //.fillMaxSize(fraction = 0.1f)
                    .background(
                        color = Color.Black.copy(alpha = .75f),
                        shape = CircleShape
                    )
            /* .border(
                        width = 2.dp,
                        shape = CircleShape,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = .25f),
                                Color.Black.copy(alpha = .25f),
                            )
                        )
                    )*/
            ) {
        /*  // Indicator highlight & shadow
                Box(
                    Modifier
                        .fillMaxSize()
                        .offset { IntOffset(0, -20) }
                        .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        .background(Color.White.copy(alpha = .10f), CircleShape)
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .offset { IntOffset(0, 20) }
                        .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        .background(Color.Black.copy(alpha = .10f), CircleShape)
                )*/
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // TODO - probably remove?
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension * 1.2f// / 2 + 8.dp.toPx()

            val colorStops = arrayOf(
                0.0f to Color.LightGray, //DarkGray,
                0.16f to Color.Red,
                0.33f to Color.LightGray,
                0.75f to Color.LightGray //DarkGray
            )

            val angleInDegrees = (angle * 360) / 1.2f
            drawArc(
                brush = Brush.sweepGradient(colorStops = colorStops), // listOf(Color.LightGray, Color.Magenta, Color.Red),
                startAngle = 120f,
                sweepAngle = angleInDegrees,
                //size = Size(radius,radius),
                useCenter = false,
                style = Stroke(width = knobSize.div(8f), cap = StrokeCap.Round)
            )
        }

        /** HANDLES */
        // TODO - reduce handle shadows/highlights - check on white theme
        Box( // Left handle
            Modifier
                .offset(handleX.dp,-handleY.dp)
                .align(Alignment.Center)
                .size(handleSize.dp)
                //.fillMaxSize(fraction = 0.1f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant,
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = 4.dp,
                    shape = CircleShape,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = .25f),
                            Color.Black.copy(alpha = .25f),
                        )
                    )
                )
        ) {
            // Left handle highlight & shadow
            Box(
                Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, -20) }
                    .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(Color.White.copy(alpha = .10f), CircleShape)
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, 20) }
                    .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(Color.Black.copy(alpha = .10f), CircleShape)
            )
        }

        Box( // Right handle
            Modifier
                .offset(-handleX.dp,-handleY.dp)
                .align(Alignment.Center)
                .size(handleSize.dp)
                //.fillMaxSize(fraction = 0.1f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant,
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = 4.dp,
                    shape = CircleShape,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = .25f),
                            Color.Black.copy(alpha = .25f),
                        )
                    )
                )
        ) {
            // Right handle highlight & shadow
            Box(
                Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, -20) }
                    .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(Color.White.copy(alpha = .10f), CircleShape)
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, 20) }
                    .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(Color.Black.copy(alpha = .10f), CircleShape)
            )
        }

        // Creating a canvas with text
        Canvas(modifier = Modifier.fillMaxSize().offset(-(knobSize/16f).dp,-(knobSize/16f).dp)) //-8.dp,-8.dp))
        {
            drawIntoCanvas {
                //val textPadding = 10.dp.toPx()
                val arcHeight = (knobSize * 1.44f).dp.toPx()
                val arcWidth = (knobSize * 1.44f).dp.toPx()

                // Path for curved Name
                val namePath = Path().apply {
                    addArc(0f, 0f /*textPadding*/, arcWidth, arcHeight, 160f, 100f)
                }
                it.nativeCanvas.drawTextOnPath(
                    //"Test name test name test name test name test name ",
                    knob.name,
                    namePath,
                    -10f,
                    -10f,
                    Paint().apply {
                        color = labelColor.toArgb()
                        textSize = (knobSize/8f).sp.toPx()
                        textAlign = Paint.Align.CENTER
                        //textSkewX = -0.15f
                        //textScaleX = 1.1f
                        isFakeBoldText = true
                        //letterSpacing = 1.1f
                    }
                )

                // Path for curved value
                val valuePath = Path().apply {
                    addArc(0f, 0f /*textPadding*/, arcWidth, arcHeight, 80f, -90f)
                }
                it.nativeCanvas.drawTextOnPath(
                    //"Test name test name test name test name test name ",//
                    "%.1f".format(angle.mapRange(0f,1f, knob.startPoint, knob.endPoint)).toFloat().toString()+knob.measure,
                    valuePath,
                    10f,
                    10f,
                    Paint().apply {
                        color = labelColor.toArgb()
                        textSize = (knobSize/8f).sp.toPx()
                        textAlign = Paint.Align.CENTER
                        //textSkewX = -0.15f
                        //textScaleX = 1.1f
                        //isFakeBoldText = true
                    }
                )

            }

        }
    }

}


@Preview("Size 80", "Regular", showBackground = true, widthDp = 250, heightDp = 250)
@Composable
fun NeuroKnobPreview() {
    NeuroPluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier,
        knobSize = 80f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        },
        value = 1f,
    )
}

@Preview("Size 100", "Regular", showBackground = true, widthDp = 250, heightDp = 250)
@Composable
fun NeuroKnobPreviewMed() {
    NeuroPluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier,
        knobSize = 100f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        },
        value = 1f,
    )
}

@Preview("Size 100", "Regular", showBackground = true, widthDp = 250, heightDp = 250)
@Composable
fun NeuroKnobPreviewLarge() {
    NeuroPluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier,
        knobSize = 150f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        },
        value = 0.8f,
    )
}

@Preview("Size 250", "Regular", showBackground = true, widthDp = 250, heightDp = 250)
@Composable
fun NeuroKnobPreviewExtraLarge() {
    NeuroPluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier,
        knobSize = 650f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        },
        value = 0.8f,
    )
}

//@PreviewFontScale
//@PreviewLightDark
//@PreviewDynamicColors
@Preview("Size 50", "Regular", showBackground = true, widthDp = 250, heightDp = 250)
@Composable
fun SmallKnob() {
    PluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier,
        knobSize = 50f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        },
        value = 0f
    )
}

@Preview("Size 100", "Regular", showBackground = true, widthDp = 150, heightDp = 200)
@Composable
fun MediumKnob() {
    PluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier,
        knobSize = 100f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        }
    )
}

@Preview("Size 150", "Regular", showBackground = true, widthDp = 150, heightDp = 200)
@Composable
fun LargeKnob() {
    PluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier,
        knobSize = 150f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        }
    )
}

@Preview("Size 80", "Radial", showBackground = false, widthDp = 150, heightDp = 200)
@Composable
fun RadialSmall() {
    RadialPluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier,
        knobSize = 80f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        }
    )
}

@Preview("Size 170", "Radial", showBackground = false, widthDp = 150, heightDp = 200)
@Composable
fun RadialMedium() {
    RadialPluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier,
        knobSize = 170f,
        //knobName = "Gain 2",
        knobColor = Color.LightGray,
        indicatorColor = Color.DarkGray,
        onValueChanged = { value ->
            // Placeholder code for handling volume change
            println("Volume changed: $value")
        }
    )
}

@Preview("Size 200", "Radial", showBackground = false, widthDp = 150, heightDp = 200)
@Composable
fun RadialLarge() {
    RadialPluginKnob(
        knob = LocalPluginsDataProvider.TestKnob.knobSet1[2],
        modifier = Modifier,
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