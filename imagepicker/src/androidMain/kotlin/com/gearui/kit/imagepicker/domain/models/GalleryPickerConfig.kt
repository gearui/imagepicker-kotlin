package com.gearui.kit.imagepicker.domain.models

import android.content.Context
import com.gearui.kit.imagepicker.presentation.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.domain.config.AndroidGalleryConfig
import com.gearui.kit.imagepicker.domain.config.CropConfig

data class GalleryPickerConfig(
    val context: Context,
    val onPhotosSelected: (List<ImageAsset>) -> Unit,
    val onError: (Exception) -> Unit,
    val onDismiss: () -> Unit,
    val allowMultiple: Boolean,
    val mimeTypes: List<String>,
    val cameraCaptureConfig: CameraCaptureConfig?,
    val cropConfig: CropConfig = CropConfig(),
    val includeExif: Boolean = false,
    val androidGalleryConfig: AndroidGalleryConfig? = null
) {
    /**
     * Gets the effective AndroidGalleryConfig, using automatic detection if not explicitly provided
     */
    fun getEffectiveAndroidGalleryConfig(): AndroidGalleryConfig {
        return androidGalleryConfig ?: AndroidGalleryConfig.forMimeTypeStrings(mimeTypes)
    }
 }
