package com.gearui.kit.imagepicker.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.gearui.kit.imagepicker.domain.models.PhotoResult

@Composable
expect fun applyCrop(
    photoResult: PhotoResult,
    cropRect: Rect,
    canvasSize: Size,
    isCircularCrop: Boolean,
    onComplete: (PhotoResult) -> Unit
)