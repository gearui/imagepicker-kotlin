package com.gearui.kit.imagepicker.presentation.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.gearui.kit.imagepicker.presentation.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.presentation.config.CameraPreviewConfig
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.domain.config.GalleryConfig
import com.gearui.kit.imagepicker.presentation.config.PermissionConfig
import com.gearui.kit.imagepicker.presentation.config.UiConfig
import com.gearui.kit.imagepicker.domain.exceptions.PhotoCaptureException
import com.gearui.kit.imagepicker.domain.models.CapturePhotoPreference
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.utils.defaultCameraPermissionDialogConfig
import com.gearui.kit.imagepicker.domain.utils.playShutterSound
import com.gearui.kit.imagepicker.presentation.ui.components.CameraCapturePreview
import com.gearui.kit.imagepicker.presentation.ui.components.GalleryPickerLauncher
import com.gearui.kit.imagepicker.presentation.ui.components.ImageConfirmationViewWithCustomButtons
import com.gearui.kit.imagepicker.presentation.ui.components.ImageCropView
import com.gearui.kit.imagepicker.presentation.ui.components.RequestCameraPermission
import com.gearui.kit.imagepicker.presentation.ui.utils.rememberCameraManager
import com.gearui.kit.imagepicker.presentation.ui.utils.rememberImagePickerViewModel
import imagepicker.imagepicker.generated.resources.Res
import imagepicker.imagepicker.generated.resources.camera_permission_permanently_denied
import org.jetbrains.compose.resources.stringResource

@Suppress("LongMethod", "LongParameterList")
@Composable
fun CameraCaptureView(
    activity: ComponentActivity,
    onPhotoResult: (ImageAsset) -> Unit,
    onPhotosSelected: ((List<ImageAsset>) -> Unit)? = null,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit = {},
    cameraCaptureConfig: CameraCaptureConfig = CameraCaptureConfig(preference = CapturePhotoPreference.QUALITY),
    galleryConfig: GalleryConfig = GalleryConfig(),
    cropConfig: CropConfig = CropConfig(),
    enableCrop: Boolean = cropConfig.enabled
) {
    val context = LocalContext.current
    var photoResult by remember { mutableStateOf<ImageAsset?>(null) }
    var showCropView by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }

    val imagePickerViewModel = rememberImagePickerViewModel()
    val cameraManager = rememberCameraManager(context, activity)

    if (cameraManager == null) {
        onError(Exception("Camera dependencies not available. Please check camera permissions and device capabilities."))
        return
    }

    DisposableEffect(Unit) {
        onDispose { cameraManager.stopCamera() }
    }

    if (!hasPermission) {
        PermissionHandler(
            customPermissionHandler = cameraCaptureConfig.permissionAndConfirmationConfig.customPermissionHandler,
            customDeniedDialog = cameraCaptureConfig.permissionAndConfirmationConfig.customDeniedDialog,
            customSettingsDialog = cameraCaptureConfig.permissionAndConfirmationConfig.customSettingsDialog,
            onPermissionGranted = { hasPermission = true },
            onError = { exception ->
                imagePickerViewModel.onError(exception)
                onError(exception)
            }
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            enableCrop && showCropView && photoResult != null -> {
                ImageCropView(
                    photoResult = photoResult!!,
                    cropConfig = cropConfig,
                    onAccept = { croppedResult: ImageAsset ->
                        showCropView = false
                        onPhotoResult(croppedResult)
                    },
                    onCancel = {
                        showCropView = false
                        photoResult = null
                    }
                )
            }

            galleryConfig.allowMultiple && onPhotosSelected != null -> {
                GalleryPickerLauncher(
                    onPhotosSelected = { results: List<ImageAsset> ->
                        onPhotosSelected(
                            results
                        )
                    },
                    onError = { exception: Exception ->
                        imagePickerViewModel.onError(exception)
                        onError(exception)
                    },
                    onDismiss = onDismiss,
                    allowMultiple = true,
                    mimeTypes = galleryConfig.mimeTypes,
                    selectionLimit = galleryConfig.selectionLimit.toLong(),
                    cameraCaptureConfig = cameraCaptureConfig,
                    cropConfig = cropConfig,
                    fileFilterDescription = "Image files",
                    includeExif = galleryConfig.includeExif
                )
            }

            photoResult == null -> {
                CameraAndPreview(
                    cameraCaptureConfig = cameraCaptureConfig,
                    galleryConfig = galleryConfig,
                    onPhotoResult = { result ->
                        photoResult = result
                        if (enableCrop) {
                            showCropView = true
                        } else {
                            playShutterSound()
                            if (cameraCaptureConfig.permissionAndConfirmationConfig.skipConfirmation) {
                                onPhotoResult(result)
                                photoResult = null
                            }
                        }
                    },
                    onError = { exception: Exception ->
                        imagePickerViewModel.onError(exception)
                        onError(exception)
                    }
                )
            }

            photoResult != null && !cameraCaptureConfig.permissionAndConfirmationConfig.skipConfirmation -> {
                ConfirmationView(
                    photoResult = photoResult!!,
                    onConfirm = { onPhotoResult(it) },
                    onRetry = { photoResult = null },
                    customConfirmationView = cameraCaptureConfig.permissionAndConfirmationConfig.customConfirmationView,
                    uiConfig = cameraCaptureConfig.uiConfig
                )
            }
        }
    }
}

@Composable
private fun PermissionHandler(
    customPermissionHandler: ((PermissionConfig) -> Unit)? = null,
    customDeniedDialog: (@Composable ((onRetry: () -> Unit) -> Unit))? = null,
    customSettingsDialog: (@Composable ((onOpenSettings: () -> Unit) -> Unit))? = null,
    onPermissionGranted: () -> Unit,
    onError: (Exception) -> Unit
) {
    val defaultConfig = PermissionConfig.createLocalizedComposable()
    val dialogConfig = defaultCameraPermissionDialogConfig().copy(
        customDeniedDialog = customDeniedDialog,
        customSettingsDialog = customSettingsDialog
    )
    val cameraPermissionPermanentlyDeniedMsg =
        stringResource(Res.string.camera_permission_permanently_denied)

    if (customPermissionHandler != null) {
        customPermissionHandler(defaultConfig)
    } else {
        RequestCameraPermission(
            dialogConfig = dialogConfig,
            onPermissionPermanentlyDenied = {
                onError(PhotoCaptureException(cameraPermissionPermanentlyDeniedMsg))
            },
            onResult = { _: Boolean -> onPermissionGranted() },
            customPermissionHandler = null
        )
    }
}

@Composable
private fun CameraAndPreview(
    cameraCaptureConfig: CameraCaptureConfig,
    galleryConfig: GalleryConfig,
    onPhotoResult: (ImageAsset) -> Unit,
    onError: (Exception) -> Unit
) {
    CameraCapturePreview(
        preference = cameraCaptureConfig.preference,
        onPhotoResult = onPhotoResult,
        onError = onError,
        previewConfig = CameraPreviewConfig(
            captureButtonSize = cameraCaptureConfig.captureButtonSize,
            uiConfig = cameraCaptureConfig.uiConfig,
            cameraCallbacks = cameraCaptureConfig.cameraCallbacks,
            galleryConfig = galleryConfig
        ),
        compressionLevel = cameraCaptureConfig.compressionLevel,
        includeExif = cameraCaptureConfig.includeExif
    )
}

@Composable
private fun ConfirmationView(
    photoResult: ImageAsset,
    onConfirm: (ImageAsset) -> Unit,
    onRetry: () -> Unit,
    customConfirmationView: (@Composable (ImageAsset, (ImageAsset) -> Unit, () -> Unit) -> Unit)? = null,
    uiConfig: UiConfig = UiConfig()
) {
    ImageConfirmationViewWithCustomButtons(
        result = photoResult,
        onConfirm = onConfirm,
        onRetry = onRetry,
        customConfirmationView = customConfirmationView,
        uiConfig = uiConfig
    )
}
