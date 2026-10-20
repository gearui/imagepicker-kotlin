package com.gearui.kit.imagepicker.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.gearui.kit.imagepicker.domain.models.ImageAsset

/**
 * WASM platform implementation of applyCrop.
 * Note: Image cropping functionality is not available in WASM platform.
 * This is a stub implementation for compatibility.
 */
@Composable
actual fun applyCrop(
    photoResult: ImageAsset,
    cropRect: Rect,
    canvasSize: Size,
    isCircularCrop: Boolean,
    onComplete: (ImageAsset) -> Unit
) {
    // Stub implementation - WASM doesn't support image cropping
    // Return the original photoResult unchanged
    onComplete(photoResult)
}
