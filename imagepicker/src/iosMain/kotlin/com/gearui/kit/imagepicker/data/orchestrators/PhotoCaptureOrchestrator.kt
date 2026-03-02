package com.gearui.kit.imagepicker.data.orchestrators

import com.gearui.kit.imagepicker.presentation.presenters.CameraPresenter
import com.gearui.kit.imagepicker.domain.utils.ViewControllerProvider
import com.gearui.kit.imagepicker.domain.exceptions.PhotoCaptureException
import com.gearui.kit.imagepicker.domain.models.PhotoResult
import com.gearui.kit.imagepicker.domain.models.CompressionLevel

/**
 * Orchestrates the presentation and handling of the camera interface on iOS.
 *
 * Provides a method to launch the camera and handle photo capture or errors.
 */
object PhotoCaptureOrchestrator {
    fun launchCamera(
        onPhotoCaptured: (PhotoResult) -> Unit,
        onError: (Exception) -> Unit,
        onDismiss: () -> Unit,
        compressionLevel: CompressionLevel? = null,
        includeExif: Boolean = false
    ) {
        try {
            val rootViewController = ViewControllerProvider.getRootViewController()
            if (rootViewController == null) {
                onError(PhotoCaptureException("Could not find root view controller"))
                return
            }
            CameraPresenter.presentCamera(
                rootViewController,
                onPhotoCaptured,
                onError,
                onDismiss,
                compressionLevel,
                includeExif
            )
        } catch (e: Exception) {
            onError(PhotoCaptureException("Failed to launch camera: ${e.message}"))
        }
    }
}
