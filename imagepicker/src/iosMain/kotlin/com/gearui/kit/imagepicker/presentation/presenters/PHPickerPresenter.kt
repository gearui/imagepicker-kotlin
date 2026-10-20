package com.gearui.kit.imagepicker.presentation.presenters

import com.gearui.kit.imagepicker.presentation.config.ImagePickerUiConstants.SELECTION_LIMIT
import com.gearui.kit.imagepicker.domain.models.CompressionLevel
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.presentation.ui.components.showPermissionDeniedDialog
import platform.UIKit.UIViewController

/**
 * Presents the PHPickerViewController for multiple photo selection on iOS 14+.
 *
 * This object manages the presentation and delegation of the PHPickerViewController.
 */
object PHPickerPresenter {
    fun presentGallery(
        viewController: UIViewController,
        onPhotoSelected: (ImageAsset) -> Unit,
        onError: (Exception) -> Unit,
        onDismiss: () -> Unit,
        selectionLimit: Long,
        compressionLevel: CompressionLevel? = null,
        includeExif: Boolean,
        onPhotosSelected: ((List<ImageAsset>) -> Unit)? = null
    ) {
        require(selectionLimit <= SELECTION_LIMIT) {"Selection limit cannot exceed $SELECTION_LIMIT"}
        
        if (includeExif) {
            checkPhotoLibraryPermission(
                onAuthorized = {
                    presentPickerViewController(
                        viewController,
                        onPhotoSelected,
                        onError,
                        onDismiss,
                        selectionLimit,
                        compressionLevel,
                        includeExif,
                        onPhotosSelected
                    )
                },
                onDenied = {
                    showPermissionDeniedDialog(viewController, onDismiss)
                }
            )
        } else {
            presentPickerViewController(
                viewController,
                onPhotoSelected,
                onError,
                onDismiss,
                selectionLimit,
                compressionLevel,
                includeExif,
                onPhotosSelected
            )
        }
    }
}



