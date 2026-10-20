package com.gearui.kit.imagepicker.presentation.presenters

import com.gearui.kit.imagepicker.domain.models.CompressionLevel
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import platform.UIKit.UIViewController

fun presentPickerViewController(
    viewController: UIViewController,
    onPhotoSelected: (ImageAsset) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit,
    selectionLimit: Long,
    compressionLevel: CompressionLevel? = null,
    includeExif: Boolean = false,
    onPhotosSelected: ((List<ImageAsset>) -> Unit)? = null
) {
    try {
        val pickerViewController = createPHPickerController(
            onPhotoSelected,
            onError,
            onDismiss,
            selectionLimit,
            compressionLevel,
            includeExif,
            onPhotosSelected
        )
        viewController.presentViewController(pickerViewController,
            animated = true, completion = null)
    } catch (e: Exception) {
        onError(Exception("Failed to present PHPicker: ${e.message}"))
    }
}