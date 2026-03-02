package com.gearui.kit.imagepicker.gearui

/**
 * GearUI mode image picker config.
 *
 * This config intentionally focuses on GearUI interaction layer.
 * Camera/gallery execution can be wired by host app callbacks during migration.
 */
data class GearImagePickerConfig(
    val title: String = "Select option",
    val takePhotoText: String = "Take photo",
    val selectFromGalleryText: String = "Select from gallery",
    val cancelText: String = "Cancel",
    val onTakePhoto: () -> Unit,
    val onSelectFromGallery: () -> Unit,
    val onCancel: (() -> Unit)? = null
)

