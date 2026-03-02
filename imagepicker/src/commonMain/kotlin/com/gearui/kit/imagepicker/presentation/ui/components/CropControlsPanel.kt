package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearui.components.button.Button
import com.gearui.components.button.ButtonShape
import com.gearui.components.button.ButtonSize
import com.gearui.components.button.ButtonTheme
import com.gearui.components.button.ButtonType
import imagepicker.imagepicker.generated.resources.Res
import imagepicker.imagepicker.generated.resources.image_crop_view_rotation_label
import imagepicker.imagepicker.generated.resources.image_crop_view_zoom_label
import com.gearui.kit.imagepicker.domain.config.CropConfig
import org.jetbrains.compose.resources.stringResource

@Composable
fun CropControlsPanel(
    isCircularCrop: Boolean,
    aspectRatio: String,
    zoomLevel: Float,
    rotationAngle: Float,
    cropConfig: CropConfig,
    onToggleCropShape: (Boolean) -> Unit,
    onAspectRatioChange: (String) -> Unit,
    onZoomChange: (Float) -> Unit,
    onRotationChange: (Float) -> Unit
) {
    val defaultPadding = 16.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A))
            .padding(defaultPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = defaultPadding),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val showBothByDefault = cropConfig.circularCrop && cropConfig.squareCrop
            val showBothFallback = !cropConfig.circularCrop && !cropConfig.squareCrop

            if (showBothByDefault || cropConfig.squareCrop || showBothFallback) {
                Box(modifier = Modifier.padding(horizontal = 2.dp)) {
                    Button(
                        text = "□",
                        onClick = { onToggleCropShape(false) },
                        theme = if (!isCircularCrop) ButtonTheme.PRIMARY else ButtonTheme.DEFAULT,
                        type = if (!isCircularCrop) ButtonType.FILL else ButtonType.OUTLINE,
                        size = ButtonSize.EXTRA_SMALL,
                        shape = ButtonShape.ROUND
                    )
                }
            }


            if (showBothByDefault || cropConfig.circularCrop || showBothFallback) {
                Box(modifier = Modifier.padding(horizontal = 2.dp)) {
                    Button(
                        text = "○",
                        onClick = { onToggleCropShape(true) },
                        theme = if (isCircularCrop) ButtonTheme.PRIMARY else ButtonTheme.DEFAULT,
                        type = if (isCircularCrop) ButtonType.FILL else ButtonType.OUTLINE,
                        size = ButtonSize.EXTRA_SMALL,
                        shape = ButtonShape.ROUND
                    )
                }
            }

            if (showBothByDefault || cropConfig.squareCrop || showBothFallback) {
                val ratios = listOf("1:1", "4:3", "16:9", "9:16")
                ratios.forEach { ratio ->
                    Box(modifier = Modifier.padding(horizontal = 2.dp)) {
                        Button(
                            text = ratio,
                            onClick = { onAspectRatioChange(ratio) },
                            disabled = isCircularCrop,
                            theme = if (aspectRatio == ratio && !isCircularCrop) ButtonTheme.PRIMARY else ButtonTheme.DEFAULT,
                            type = if (aspectRatio == ratio && !isCircularCrop) ButtonType.FILL else ButtonType.OUTLINE,
                            size = ButtonSize.EXTRA_SMALL,
                            shape = ButtonShape.ROUND
                        )
                    }
                }
            }
        }

        Column {
            Text(
                text = stringResource(Res.string.image_crop_view_zoom_label),
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Slider(
                value = zoomLevel,
                onValueChange = onZoomChange,
                valueRange = 0.5f..2.5f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = stringResource(Res.string.image_crop_view_rotation_label),
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Slider(
                value = rotationAngle,
                onValueChange = onRotationChange,
                valueRange = -180f..180f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White
                )
            )
        }
    }
}
