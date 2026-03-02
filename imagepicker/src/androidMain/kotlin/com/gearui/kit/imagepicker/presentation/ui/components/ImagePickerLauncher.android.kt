package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.gearui.kit.imagepicker.presentation.ui.screens.CameraCaptureView
import com.gearui.kit.imagepicker.domain.config.ImagePickerConfig
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.gearui.GearImagePickerConfig
import com.gearui.kit.imagepicker.gearui.GearImagePickerLauncher
import com.gearui.kit.imagepicker.presentation.ui.extensions.activity
import org.jetbrains.compose.resources.stringResource
import imagepicker.imagepicker.generated.resources.Res
import imagepicker.imagepicker.generated.resources.invalid_context_error

@Suppress("FunctionNaming")
@Composable
actual fun ImagePickerLauncher(
    config: ImagePickerConfig
) {
    val context = LocalContext.current
    val activity = context.activity
    if (activity !is ComponentActivity) {
        config.onError(Exception(stringResource(Res.string.invalid_context_error)))
        return
    }

    var showPicker by remember { mutableStateOf(!config.directCameraLaunch) }
    var launchCamera by remember { mutableStateOf(config.directCameraLaunch) }
    var launchGallery by remember { mutableStateOf(false) }

    if (showPicker) {
        if (config.customPickerDialog != null) {
            config.customPickerDialog.invoke(
                {
                    showPicker = false
                    launchCamera = true
                },
                {
                    showPicker = false
                    launchGallery = true
                },
                {
                    showPicker = false
                    config.onDismiss()
                }
            )
        } else {
            GearImagePickerLauncher(
                visible = showPicker,
                config = GearImagePickerConfig(
                    title = config.dialogTitle,
                    takePhotoText = config.takePhotoText,
                    selectFromGalleryText = config.selectFromGalleryText,
                    cancelText = config.cancelText,
                    onTakePhoto = {
                        showPicker = false
                        launchCamera = true
                    },
                    onSelectFromGallery = {
                        showPicker = false
                        launchGallery = true
                    },
                    onCancel = {
                        showPicker = false
                        config.onDismiss()
                    }
                )
            )
        }
    }

    if (launchCamera) {
        CameraCaptureView(
            activity = activity,
            onPhotoResult = { result ->
                launchCamera = false
                config.onPhotoCaptured(result)
            },
            onPhotosSelected = { selected ->
                launchCamera = false
                config.onPhotosSelected?.invoke(selected)
            },
            onError = { exception ->
                launchCamera = false
                config.onError(exception)
            },
            onDismiss = {
                launchCamera = false
                config.onDismiss()
            },
            cameraCaptureConfig = config.cameraCaptureConfig.copy(
                preference = config.cameraCaptureConfig.preference,
                includeExif = config.cameraCaptureConfig.includeExif,
                cropConfig = if (config.cameraCaptureConfig.cropConfig.enabled) {
                    config.cameraCaptureConfig.cropConfig
                } else if (config.enableCrop) {
                    CropConfig(
                        enabled = true,
                        circularCrop = true,
                        squareCrop = true
                    )
                } else {
                    config.cameraCaptureConfig.cropConfig
                }
            ),
            enableCrop = config.cameraCaptureConfig.cropConfig.enabled || config.enableCrop
        )
    }

    if (launchGallery) {
        GalleryPickerLauncher(
            onPhotosSelected = { results ->
                launchGallery = false
                config.onPhotosSelected?.invoke(results)
            },
            onError = {
                launchGallery = false
                config.onError(it)
            },
            onDismiss = {
                launchGallery = false
                config.onDismiss()
            },
            allowMultiple = config.cameraCaptureConfig.galleryConfig.allowMultiple,
            mimeTypes = config.cameraCaptureConfig.galleryConfig.mimeTypes,
            selectionLimit = config.cameraCaptureConfig.galleryConfig.selectionLimit.toLong(),
            cameraCaptureConfig = config.cameraCaptureConfig,
            enableCrop = config.cameraCaptureConfig.cropConfig.enabled || config.enableCrop,
            includeExif = config.cameraCaptureConfig.galleryConfig.includeExif
        )
    }
}
