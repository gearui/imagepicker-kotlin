package com.gearui.kit.imagepicker.domain.models

/**
 * Unified image result model for both camera capture and gallery selection.
 */
data class ImageAsset(
    val uri: String,
    val width: Int? = null,
    val height: Int? = null,
    val fileName: String? = null,
    val fileSize: Long? = null,
    val mimeType: String? = null,
    val exif: ExifData? = null
)
