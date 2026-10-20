package com.gearui.kit.imagepicker.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.gearui.kit.imagepicker.domain.models.ImageAsset

@Composable
expect fun applyCrop(
    photoResult: ImageAsset,
    cropRect: Rect,
    canvasSize: Size,
    isCircularCrop: Boolean,
    onComplete: (ImageAsset) -> Unit
)