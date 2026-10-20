package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.presentation.config.ImagePickerConfig
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.ImagePickerResult
import com.gearui.kit.imagepicker.gearui.GearImagePickerConfig
import com.gearui.kit.imagepicker.gearui.GearImagePickerLauncher
import com.gearui.kit.imagepicker.presentation.ui.screens.CameraCaptureView
import com.gearui.kit.imagepicker.presentation.ui.extensions.activity
import imagepicker.imagepicker.generated.resources.Res
import imagepicker.imagepicker.generated.resources.invalid_context_error
import org.jetbrains.compose.resources.stringResource

private fun ImageAsset.toPickerSingleResult(): ImagePickerResult = ImagePickerResult.Single(this)

@Suppress("FunctionNaming")
@Composable
actual fun ImagePickerLauncher(
    config: ImagePickerConfig,
    visible: Boolean
) {
    val currentConfig by rememberUpdatedState(config)
    if (!visible) {
        return
    }

    val context = LocalContext.current
    val activity = context.activity
    if (activity !is ComponentActivity) {
        currentConfig.onError(Exception(stringResource(Res.string.invalid_context_error)))
        return
    }

    var showPicker by remember { mutableStateOf(false) }
    var launchCamera by remember { mutableStateOf(false) }
    var launchGallery by remember { mutableStateOf(false) }

    LaunchedEffect(visible, config.directCameraLaunch, config.directGalleryLaunch) {
        if (visible) {
            showPicker = !currentConfig.directCameraLaunch && !currentConfig.directGalleryLaunch
            launchCamera = currentConfig.directCameraLaunch
            launchGallery = currentConfig.directGalleryLaunch
        } else {
            showPicker = false
            launchCamera = false
            launchGallery = false
        }
    }

    if (showPicker) {
        val customPickerDialog = currentConfig.customPickerDialog
        if (customPickerDialog != null) {
            customPickerDialog(
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
                    currentConfig.onDismiss()
                }
            )
        } else {
            GearImagePickerLauncher(
                visible = showPicker,
                config = GearImagePickerConfig(
                    title = currentConfig.dialogTitle,
                    takePhotoText = currentConfig.takePhotoText,
                    selectFromGalleryText = currentConfig.selectFromGalleryText,
                    cancelText = currentConfig.cancelText,
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
                        currentConfig.onDismiss()
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
                currentConfig.onResult(result.toPickerSingleResult())
            },
            onPhotosSelected = { selected ->
                launchCamera = false
                currentConfig.onResult(ImagePickerResult.Multiple(selected))
            },
            onError = { exception ->
                launchCamera = false
                currentConfig.onError(exception)
            },
            onDismiss = {
                launchCamera = false
                currentConfig.onDismiss()
            },
            cameraCaptureConfig = currentConfig.cameraCaptureConfig.copy(
                preference = currentConfig.cameraCaptureConfig.preference,
                includeExif = currentConfig.cameraCaptureConfig.includeExif
            ),
            galleryConfig = currentConfig.galleryConfig,
            cropConfig = if (currentConfig.cropConfig.enabled) currentConfig.cropConfig else CropConfig(enabled = false),
            enableCrop = currentConfig.cropConfig.enabled
        )
    }

    if (launchGallery) {
        GalleryPickerLauncher(
            onPhotosSelected = { results ->
                launchGallery = false
                val result = if (results.size == 1) {
                    ImagePickerResult.Single(results.first())
                } else {
                    ImagePickerResult.Multiple(results)
                }
                currentConfig.onResult(result)
            },
            onError = {
                launchGallery = false
                currentConfig.onError(it)
            },
            onDismiss = {
                launchGallery = false
                currentConfig.onDismiss()
            },
            allowMultiple = currentConfig.galleryConfig.allowMultiple,
            mimeTypes = currentConfig.galleryConfig.mimeTypes,
            selectionLimit = currentConfig.galleryConfig.selectionLimit.toLong(),
            cameraCaptureConfig = currentConfig.cameraCaptureConfig,
            cropConfig = currentConfig.cropConfig,
            includeExif = currentConfig.galleryConfig.includeExif
        )
    }
}
