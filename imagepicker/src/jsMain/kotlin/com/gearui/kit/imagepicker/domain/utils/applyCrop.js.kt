package com.gearui.kit.imagepicker.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.gearui.kit.imagepicker.domain.models.ImageAsset

@Composable
actual fun applyCrop(
    photoResult: ImageAsset,
    cropRect: Rect,
    canvasSize: Size,
    isCircularCrop: Boolean,
    onComplete: (ImageAsset) -> Unit
) {
    // En JS/Web, el recorte se puede implementar usando Canvas API
    // Por ahora devolvemos el resultado original
    // TODO: Implementar recorte usando HTML5 Canvas
    onComplete(photoResult)
}
