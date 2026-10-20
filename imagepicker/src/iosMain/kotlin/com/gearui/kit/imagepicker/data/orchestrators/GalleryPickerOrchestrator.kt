package com.gearui.kit.imagepicker.data.orchestrators

import com.gearui.kit.imagepicker.presentation.presenters.GalleryPresenter
import com.gearui.kit.imagepicker.domain.utils.ViewControllerProvider
import com.gearui.kit.imagepicker.presentation.config.ImagePickerUiConstants.SELECTION_LIMIT
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.CompressionLevel
import com.gearui.kit.imagepicker.presentation.presenters.PHPickerPresenter

/**
 * Orchestrates the presentation and handling of the gallery picker on iOS.
 *
 * Provides a method to launch the gallery and handle photo selection or errors.
 */
object GalleryPickerOrchestrator {
    fun launchGallery(
        onPhotoSelected: (ImageAsset) -> Unit,
        onError: (Exception) -> Unit,
        onDismiss: () -> Unit,
        allowMultiple: Boolean = false,
        selectionLimit: Long = SELECTION_LIMIT,
        compressionLevel: CompressionLevel? = null,
        includeExif: Boolean = false,
        onPhotosSelected: ((List<ImageAsset>) -> Unit)? = null
    ) {
        try {
            val rootViewController = ViewControllerProvider.getRootViewController()
            if (rootViewController == null) {
                onError(Exception("Could not find root view controller"))
                return
            }
            if (allowMultiple) {
                PHPickerPresenter.presentGallery(
                    rootViewController,
                    onPhotoSelected,
                    onError,
                    onDismiss,
                    selectionLimit,
                    compressionLevel,
                    includeExif,
                    onPhotosSelected
                )
            } else {
                GalleryPresenter.presentGallery(
                    rootViewController,
                    onPhotoSelected,
                    onError,
                    onDismiss,
                    compressionLevel,
                    includeExif
                )
            }
        } catch (e: Exception) {
            onError(Exception("Failed to launch gallery: ${e.message}"))
        }
    }
}
