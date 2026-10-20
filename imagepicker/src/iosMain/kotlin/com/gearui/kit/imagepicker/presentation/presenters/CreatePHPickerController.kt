package com.gearui.kit.imagepicker.presentation.presenters

import com.gearui.kit.imagepicker.data.delegates.PHPickerDelegate
import com.gearui.kit.imagepicker.domain.models.CompressionLevel
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import platform.Photos.PHPhotoLibrary
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerViewController

private var strongPickerDelegate: PHPickerDelegate? = null

 fun createPHPickerController(
    onPhotoSelected: (ImageAsset) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit,
    selectionLimit: Long,
    compressionLevel: CompressionLevel? = null,
    includeExif: Boolean = false,
    onPhotosSelected: ((List<ImageAsset>) -> Unit)? = null
): PHPickerViewController {
    val configuration = PHPickerConfiguration(
        photoLibrary = PHPhotoLibrary.sharedPhotoLibrary()
    )
    configuration.selectionLimit = selectionLimit
    configuration.filter = PHPickerFilter.imagesFilter

    val cleanup = { 
        strongPickerDelegate = null
    }
    val wrappedOnPhotoSelected: (ImageAsset) -> Unit = { result ->
        onPhotoSelected(result)
        cleanup()
    }
    val wrappedOnPhotosSelected: ((List<ImageAsset>) -> Unit)? = if (onPhotosSelected != null) {
        { results ->
            onPhotosSelected(results)
            cleanup()
        }
    } else null
    
    val wrappedOnError: (Exception) -> Unit = { error ->
        onError(error)
        cleanup()
    }
    val wrappedOnDismiss: () -> Unit = {
        onDismiss()
        cleanup()
    }
    
    strongPickerDelegate = PHPickerDelegate(
        wrappedOnPhotoSelected,
        wrappedOnPhotosSelected,
        wrappedOnError,
        wrappedOnDismiss,
        compressionLevel,
        includeExif
    )

    val pickerDelegate = strongPickerDelegate ?: return PHPickerViewController(configuration).apply {
        delegate = null
    }

    return DismissalAwarePHPickerViewController.createPickerViewController(configuration, pickerDelegate).apply {
        delegate = pickerDelegate
    }
}