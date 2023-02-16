package com.example.slider.ui.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    thumbSize: Dp = 36.dp,
    thumbColor: Color,
    trackColor: Color,
    trackMovementColor: Color,
    thumbShape: Shape = CircleShape,
    onValueChange: (Float) -> Unit
) {

    Column(verticalArrangement = Arrangement.Center, modifier = modifier) {
        val maxWidth = remember {
            mutableStateOf(0f)
        }
        val swipeAbleState = remember {
            mutableStateOf(Offset(thumbSize.value, 0f))
        }

        val changeX = remember {
            mutableStateOf(0f)
        }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            val minPx = 0f
            val maxPx = constraints.maxWidth.toFloat()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .heightIn(min = 8.dp)
                    .background(trackColor, shape = RoundedCornerShape(32.dp))
                    .onGloballyPositioned {
                        maxWidth.value = it.boundsInRoot().maxDimension
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, _ ->

                                swipeAbleState.value =
                                    if (change.position.x <= maxWidth.value && change.position.x >= 0f) change.position else Offset(
                                        x = if (change.position.x <= thumbSize.value) thumbSize.value else maxWidth.value,
                                        0f
                                    )
                                changeX.value = ((change.position.x.coerceIn(minPx, maxPx))) / maxPx
                                onValueChange(changeX.value)
                            }
                        )

                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { offset ->
                                changeX.value = (offset.x.coerceIn(minPx, maxPx)) / maxPx
                                onValueChange(changeX.value)
                                swipeAbleState.value = if (offset.x <= thumbSize.value) Offset(
                                    thumbSize.value,
                                    0f
                                ) else offset
                            }
                        )
                    }
                    .background(
                        brush = Brush.linearGradient(
                            1.0f to trackMovementColor,
                            0.0f to trackColor,
                            start = Offset(0.0f, 0.0f),
                            end = Offset(swipeAbleState.value.x, 0f)
                        )
                    )
            )
            Spacer(
                modifier = Modifier
                    .offset { IntOffset(swipeAbleState.value.x.roundToInt(), 0) }
                    .shadow(1.dp, shape = thumbShape)
                    .size(thumbSize)
                    .then(
                        Modifier.background(thumbColor)
                    )
                    .align(Alignment.CenterStart)
            )

        }

    }

}