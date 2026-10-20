package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.gearui.kit.imagepicker.gearui.GearImagePickerConfig
import com.gearui.kit.imagepicker.gearui.GearImagePickerLauncher
import com.gearui.kit.imagepicker.data.orchestrators.GalleryPickerOrchestrator
import com.gearui.kit.imagepicker.data.orchestrators.PhotoCaptureOrchestrator
import com.gearui.kit.imagepicker.presentation.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.presentation.config.CameraPermissionDialogConfig
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.presentation.config.ImagePickerConfig
import com.gearui.kit.imagepicker.domain.models.CompressionLevel
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.ImagePickerResult
import imagepicker.imagepicker.generated.resources.Res
import imagepicker.imagepicker.generated.resources.camera_permission_denied
import imagepicker.imagepicker.generated.resources.camera_permission_denied_description
import imagepicker.imagepicker.generated.resources.camera_permission_description
import imagepicker.imagepicker.generated.resources.camera_permission_required
import imagepicker.imagepicker.generated.resources.grant_permission
import imagepicker.imagepicker.generated.resources.open_settings
import org.jetbrains.compose.resources.stringResource


@Suppress("FunctionNaming","TrailingWhitespace")
@Composable
actual fun ImagePickerLauncher(
    config: ImagePickerConfig,
    visible: Boolean
) {
    if (!visible) {
        return
    }

    var showDialog by remember { mutableStateOf(false) }
    var askCameraPermission by remember { mutableStateOf(false) }
    var launchCamera by remember { mutableStateOf(false) }
    var launchGallery by remember { mutableStateOf(false) }
    
    var selectedPhotoForCrop by remember { mutableStateOf<ImageAsset?>(null) }
    var showCropView by remember { mutableStateOf(false) }

    LaunchedEffect(visible, config.directCameraLaunch, config.directGalleryLaunch) {
        if (visible) {
            showDialog = !config.directCameraLaunch && !config.directGalleryLaunch
            askCameraPermission = config.directCameraLaunch
            launchCamera = false
            launchGallery = config.directGalleryLaunch
        } else {
            showDialog = false
            askCameraPermission = false
            launchCamera = false
            launchGallery = false
            showCropView = false
            selectedPhotoForCrop = null
        }
    }
    
    val onCameraPermissionGranted = {
        askCameraPermission = false
        launchCamera = true
    }
    val onCameraPermissionDenied = {
        askCameraPermission = false
        config.onDismiss()
    }
    val onCameraFinished = { launchCamera = false }
    val onGalleryFinished = { launchGallery = false }
    
    if (showDialog) {
        if (config.customPickerDialog != null) {
            config.customPickerDialog.invoke(
                {
                    showDialog = false
                    askCameraPermission = true
                },
                {
                    showDialog = false
                    launchGallery = true
                },
                {
                    showDialog = false
                    config.onDismiss()
                }
            )
        } else {
            GearImagePickerLauncher(
                visible = showDialog,
                config = GearImagePickerConfig(
                    title = config.dialogTitle,
                    takePhotoText = config.takePhotoText,
                    selectFromGalleryText = config.selectFromGalleryText,
                    cancelText = config.cancelText,
                    onTakePhoto = {
                        showDialog = false
                        askCameraPermission = true
                    },
                    onSelectFromGallery = {
                        showDialog = false
                        launchGallery = true
                    },
                    onCancel = {
                        showDialog = false
                        config.onDismiss()
                    }
                )
            )
        }
    }

    if (askCameraPermission) {
        handleCameraPermission(
            cameraCaptureConfig = config.cameraCaptureConfig,
            onGranted = onCameraPermissionGranted,
            onDenied = onCameraPermissionDenied
        )
    }

    if (launchCamera) {
        launchCameraInternal(
            onPhotoCaptured = { result ->
                val shouldShowCrop = config.cropConfig.enabled
                if (shouldShowCrop) {
                    selectedPhotoForCrop = result
                    showCropView = true
                } else {
                    config.onResult(ImagePickerResult.Single(result))
                }
            },
            onError = config.onError,
            onDismiss = config.onDismiss,
            onFinish = onCameraFinished,
            compressionLevel = config.cameraCaptureConfig.compressionLevel,
            includeExif = config.cameraCaptureConfig.includeExif
        )
    }

    if (launchGallery) {
        launchGalleryInternal(
            onPhotoSelected = { result ->
                val shouldShowCrop = config.cropConfig.enabled
                if (shouldShowCrop) {
                    selectedPhotoForCrop = result
                    showCropView = true
                } else {
                    if (config.galleryConfig.allowMultiple) {
                        config.onResult(ImagePickerResult.Multiple(listOf(result)))
                    } else {
                        config.onResult(ImagePickerResult.Single(result))
                    }
                }
            },
            onError = config.onError,
            onDismiss = config.onDismiss,
            onFinish = onGalleryFinished,
            compressionLevel = config.cameraCaptureConfig.compressionLevel
        )
    }
    
    if (showCropView && selectedPhotoForCrop != null) {        
        ImageCropView(
            photoResult = selectedPhotoForCrop!!,
            cropConfig = if (config.cropConfig.enabled) config.cropConfig else CropConfig(enabled = false),
            onAccept = { croppedResult ->
                config.onResult(ImagePickerResult.Single(croppedResult))
                showCropView = false
                selectedPhotoForCrop = null
            },
            onCancel = {
                showCropView = false
                selectedPhotoForCrop = null
                config.onDismiss()
            }
        )
    }
}



@Composable
private fun handleCameraPermission(
    cameraCaptureConfig: CameraCaptureConfig,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    val dialogConfig = CameraPermissionDialogConfig(
        titleDialogConfig = stringResource(Res.string.camera_permission_required),
        descriptionDialogConfig = stringResource(Res.string.camera_permission_description),
        btnDialogConfig = stringResource(Res.string.grant_permission),
        titleDialogDenied = stringResource(Res.string.camera_permission_denied),
        descriptionDialogDenied = stringResource(Res.string.camera_permission_denied_description),
        btnDialogDenied = stringResource(Res.string.open_settings),
        customDeniedDialog = cameraCaptureConfig.permissionAndConfirmationConfig.customDeniedDialog,
        customSettingsDialog = cameraCaptureConfig.permissionAndConfirmationConfig.customSettingsDialog,
        cancelButtonText = cameraCaptureConfig.permissionAndConfirmationConfig.cancelButtonTextIOS,
        onCancelPermissionConfigIOS = cameraCaptureConfig.permissionAndConfirmationConfig.onCancelPermissionConfigIOS
    )
    RequestCameraPermission(
        dialogConfig = dialogConfig,
        onPermissionPermanentlyDenied = {},
        onResult = { granted: Boolean -> if (granted) onGranted() else onDenied() },
        customPermissionHandler = null
    )
}

@Composable
private fun launchCameraInternal(
    onPhotoCaptured: (ImageAsset) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit,
    onFinish: () -> Unit,
    compressionLevel: CompressionLevel? = null,
    includeExif: Boolean = false
) {
    val currentOnPhotoCaptured by rememberUpdatedState(onPhotoCaptured)
    val currentOnError by rememberUpdatedState(onError)
    val currentOnDismiss by rememberUpdatedState(onDismiss)
    val currentOnFinish by rememberUpdatedState(onFinish)
    LaunchedEffect(Unit) {
        PhotoCaptureOrchestrator.launchCamera(
            onPhotoCaptured = {
                currentOnPhotoCaptured(it)
                currentOnFinish()
            },
            onError = {
                currentOnError(it)
                currentOnFinish()
            },
            onDismiss = {
                currentOnDismiss()
                currentOnFinish()
            },
            compressionLevel = compressionLevel,
            includeExif = includeExif
        )
    }
}

@Composable
private fun launchGalleryInternal(
    onPhotoSelected: (ImageAsset) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit,
    onFinish: () -> Unit,
    compressionLevel: CompressionLevel? = null
) {
    val currentOnPhotoSelected by rememberUpdatedState(onPhotoSelected)
    val currentOnError by rememberUpdatedState(onError)
    val currentOnDismiss by rememberUpdatedState(onDismiss)
    val currentOnFinish by rememberUpdatedState(onFinish)
    LaunchedEffect(Unit) {
        GalleryPickerOrchestrator.launchGallery(
            onPhotoSelected = {
                currentOnPhotoSelected(it)
                currentOnFinish()
            },
            onError = {
                currentOnError(it)
                currentOnFinish()
            },
            onDismiss = {
                currentOnDismiss()
                currentOnFinish()
            },
            compressionLevel = compressionLevel
        )
    }
}
