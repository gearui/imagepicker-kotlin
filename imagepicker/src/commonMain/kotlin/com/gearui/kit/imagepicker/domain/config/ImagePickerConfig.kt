package com.gearui.kit.imagepicker.domain.config

import com.gearui.kit.imagepicker.domain.models.MimeType

/**
 * Pure gallery selection options shared across platforms.
 */
data class GalleryConfig(
    val allowMultiple: Boolean = false,
    val mimeTypes: List<MimeType> = listOf(MimeType.IMAGE_ALL),
    val selectionLimit: Int = 30,
    val includeExif: Boolean = false
)

/**
 * Pure crop options that are independent from any UI toolkit.
 */
data class CropConfig(
    val enabled: Boolean = false,
    val aspectRatioLocked: Boolean = false,
    val circularCrop: Boolean = true,
    val squareCrop: Boolean = true,
    val freeformCrop: Boolean = false
)
