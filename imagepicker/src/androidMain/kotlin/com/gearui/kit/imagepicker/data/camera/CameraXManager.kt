package com.gearui.kit.imagepicker.data.camera

import androidx.camera.view.PreviewView
import com.gearui.kit.imagepicker.domain.models.CompressionLevel
import com.gearui.kit.imagepicker.domain.models.CapturePhotoPreference
import com.gearui.kit.imagepicker.domain.models.ImageAsset

/**
 * High-level manager for camera operations, providing an interface to start, stop, and control the camera.
 */
class CameraXManager(
    private val cameraController: CameraController,
    private val imageProcessor: com.gearui.kit.imagepicker.data.processors.ImageProcessor
) {

    suspend fun startCamera(
        previewView: PreviewView,
        preference: CapturePhotoPreference
    ){
        cameraController.startCamera(previewView,preference)
    }
    fun takePicture(
        onPhotoResult: (ImageAsset) -> Unit,
        onError: (Exception) -> Unit,
        compressionLevel: CompressionLevel? = null,
        includeExif: Boolean = false
    ){
        cameraController.takePicture(
            onImageCaptured = { imageFile, cameraType ->
                imageProcessor.processImage(
                    imageFile = imageFile,
                    cameraType = cameraType,
                    compressionLevel = compressionLevel,
                    includeExif = includeExif,
                    onPhotoCaptured = onPhotoResult,
                    onError = onError
                )
            },
            onError = onError
        )
    }
    fun stopCamera(){
        cameraController.stopCamera()
    }
    fun setFlashMode(mode: CameraController.FlashMode) {
        cameraController.setFlashMode(mode)
    }
    fun switchCamera() {
        cameraController.switchCamera()
    }

    val flashModes: List<CameraController.FlashMode>
        get() = CameraController.FlashMode.entries
}
