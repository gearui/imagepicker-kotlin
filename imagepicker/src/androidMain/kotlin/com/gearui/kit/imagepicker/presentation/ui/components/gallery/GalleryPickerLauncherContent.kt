package com.gearui.kit.imagepicker.presentation.ui.components.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.domain.models.GalleryPickerConfig
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.presentation.ui.components.ImageCropView

@Composable
fun GalleryPickerLauncherContent(config: GalleryPickerConfig) {
    val currentConfig by rememberUpdatedState(config)
    var shouldLaunch by remember { mutableStateOf(false) }
    var selectedPhotoForConfirmation by remember { mutableStateOf<ImageAsset?>(null) }
    var showCropView by remember { mutableStateOf(false) }

    val shouldShowCrop = config.cropConfig.enabled
    val effectiveGalleryConfig = remember(config.mimeTypes) { config.getEffectiveAndroidGalleryConfig() }

    val singlePickerLauncher = if (effectiveGalleryConfig.forceGalleryOnly) {
        rememberGalleryOnlyPickerLauncher(
            config.context,
            { photoResult ->
                if (config.cameraCaptureConfig?.permissionAndConfirmationConfig?.customConfirmationView != null) {
                    selectedPhotoForConfirmation = photoResult
                } else if (shouldShowCrop) {
                    selectedPhotoForConfirmation = photoResult
                    showCropView = true
                } else {
                    config.onPhotosSelected(listOf(photoResult))
                }
            },
            config.onError,
            config.onDismiss,
            config.cameraCaptureConfig?.compressionLevel,
            config.includeExif
        )
    } else {
        rememberSinglePickerLauncher(
            config.context,
            { photoResult ->
                if (config.cameraCaptureConfig?.permissionAndConfirmationConfig?.customConfirmationView != null) {
                    selectedPhotoForConfirmation = photoResult
                } else if (shouldShowCrop) {
                    selectedPhotoForConfirmation = photoResult
                    showCropView = true
                } else {
                    config.onPhotosSelected(listOf(photoResult))
                }
            },
            config.onError,
            config.onDismiss,
            config.cameraCaptureConfig?.compressionLevel,
            config.includeExif
        )
    }

    val multiplePickerLauncher = if (effectiveGalleryConfig.forceGalleryOnly) {
        rememberGalleryOnlyMultiplePickerLauncher(
            config.context,
            config.onPhotosSelected,
            config.onError,
            config.onDismiss,
            config.cameraCaptureConfig?.compressionLevel,
            config.includeExif
        )
    } else {
        rememberMultiplePickerLauncher(
            config.context,
            config.onPhotosSelected,
            config.onError,
            config.onDismiss,
            config.cameraCaptureConfig?.compressionLevel,
            config.includeExif
        )
    }
    val currentSinglePickerLauncher by rememberUpdatedState(singlePickerLauncher)
    val currentMultiplePickerLauncher by rememberUpdatedState(multiplePickerLauncher)

    LaunchedEffect(shouldLaunch) {
        if (shouldLaunch) {
            try {
                val mimeType = if (effectiveGalleryConfig.forceGalleryOnly) {
                    "image/*"
                } else {
                    when {
                        currentConfig.mimeTypes.size > 1 -> "*/*"
                        currentConfig.mimeTypes.any { it.contains("application/pdf", ignoreCase = true) } -> "*/*"
                        else -> currentConfig.mimeTypes.firstOrNull() ?: "image/*"
                    }
                }

                if (currentConfig.allowMultiple) {
                    currentMultiplePickerLauncher.launch(mimeType)
                } else {
                    currentSinglePickerLauncher.launch(mimeType)
                }
                shouldLaunch = false
            } catch (e: Exception) {
                currentConfig.onError(e)
            }
        }
    }

    LaunchedEffect(Unit) {
        shouldLaunch = true
    }

    selectedPhotoForConfirmation?.let { photoResult ->
        if (showCropView) {
            ImageCropView(
                photoResult = photoResult,
                cropConfig = if (config.cropConfig.enabled) config.cropConfig else CropConfig(
                    enabled = true,
                    circularCrop = true,
                    squareCrop = true
                ),
                onAccept = { croppedResult ->
                    config.onPhotosSelected(listOf(croppedResult))
                    selectedPhotoForConfirmation = null
                    showCropView = false
                },
                onCancel = {
                    selectedPhotoForConfirmation = null
                    showCropView = false
                    shouldLaunch = true
                }
            )
        } else {
            config.cameraCaptureConfig?.permissionAndConfirmationConfig?.customConfirmationView?.invoke(
                photoResult,
                { confirmedResult ->
                    config.onPhotosSelected(listOf(confirmedResult))
                    selectedPhotoForConfirmation = null
                },
                {
                    selectedPhotoForConfirmation = null
                    shouldLaunch = true
                }
            )
        }
    }
}
