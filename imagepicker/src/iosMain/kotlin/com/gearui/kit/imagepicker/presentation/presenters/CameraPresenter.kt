package com.gearui.kit.imagepicker.presentation.presenters

import com.gearui.kit.imagepicker.data.delegates.CameraDelegate
import com.gearui.kit.imagepicker.domain.exceptions.PhotoCaptureException
import com.gearui.kit.imagepicker.domain.models.PhotoResult
import com.gearui.kit.imagepicker.domain.models.CompressionLevel
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UIViewController

/**
 * Presents the camera interface on iOS and handles photo capture results.
 *
 * This object manages the presentation and delegation of the camera view controller.
 */
object CameraPresenter {

    private var cameraDelegate: CameraDelegate? = null

    fun presentCamera(
        viewController: UIViewController,
        onPhotoCaptured: (PhotoResult) -> Unit,
        onError: (Exception) -> Unit,
        onDismiss: () -> Unit,
        compressionLevel: CompressionLevel? = null,
        includeExif: Boolean = false
    ) {
        try {
            // Check if camera is available (simulators don't have cameras)
            if (!UIImagePickerController.isSourceTypeAvailable(
                UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            )) {
                val errorMessage = "📱 Camera is not available on this device. " +
                    "The iOS Simulator does not support camera functionality. " +
                    "Please test camera features on a physical iOS device."
                
                println("⚠️ $errorMessage")
                
                onError(PhotoCaptureException(errorMessage))
                // Call onDismiss to ensure proper cleanup
                onDismiss()
                return
            }
            
            val imagePickerController = createImagePickerController(
                onPhotoCaptured,
                onError,
                onDismiss,
                compressionLevel,
                includeExif
            )
            viewController.presentViewController(imagePickerController, animated = true, completion = null)
        } catch (e: Exception) {
            val errorMessage = "Failed to present camera: ${e.message}"
            println("❌ Camera presentation error: $errorMessage")
            onError(PhotoCaptureException(errorMessage))
            onDismiss()
        }
    }

    private fun createImagePickerController(
        onPhotoCaptured: (PhotoResult) -> Unit,
        onError: (Exception) -> Unit,
        onDismiss: () -> Unit,
        compressionLevel: CompressionLevel? = null,
        includeExif: Boolean = false
    ): UIImagePickerController {
        return UIImagePickerController().apply {
            sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            allowsEditing = false
            modalPresentationStyle = UIModalPresentationFullScreen

            val cleanup = { cameraDelegate = null }
            val wrappedOnPhotoCaptured: (PhotoResult) -> Unit = { result ->
                onPhotoCaptured(result)
                cleanup()
            }
            val wrappedOnError: (Exception) -> Unit = { error ->
                onError(error)
                cleanup()
            }
            val wrappedOnDismiss: () -> Unit = {
                onDismiss()
                cleanup()
            }
            cameraDelegate = CameraDelegate(
                wrappedOnPhotoCaptured,
                wrappedOnError,
                wrappedOnDismiss,
                compressionLevel,
                includeExif
            )
            delegate = cameraDelegate
        }
    }
}
