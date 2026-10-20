package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberUpdatedState
import com.gearui.kit.imagepicker.data.orchestrators.GalleryPickerOrchestrator
import com.gearui.kit.imagepicker.presentation.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.MimeType

@Suppress("LongParameterList")
@Composable
actual fun GalleryPickerLauncher(
    onPhotosSelected: (List<ImageAsset>) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit,
    allowMultiple: Boolean,
    mimeTypes: List<MimeType>,
    selectionLimit: Long,
    cameraCaptureConfig: CameraCaptureConfig?,
    cropConfig: CropConfig,
    fileFilterDescription: String,
    includeExif: Boolean
) {
    var selectedPhotoForCrop by remember { mutableStateOf<ImageAsset?>(null) }
    var showCropView by remember { mutableStateOf(false) }
    val currentOnPhotosSelected by rememberUpdatedState(onPhotosSelected)
    val currentOnError by rememberUpdatedState(onError)
    val currentOnDismiss by rememberUpdatedState(onDismiss)
    
    LaunchedEffect(Unit) {
        val compressionLevel = cameraCaptureConfig?.compressionLevel
        
        if (allowMultiple) {
            GalleryPickerOrchestrator.launchGallery(
                onPhotoSelected = { result ->
                },
                onError = currentOnError,
                onDismiss = currentOnDismiss,
                allowMultiple = true,
                selectionLimit = selectionLimit,
                compressionLevel = compressionLevel,
                includeExif = includeExif,
                onPhotosSelected = { results ->
                    if (cropConfig.enabled && results.size == 1) {
                        selectedPhotoForCrop = results.first()
                        showCropView = true
                    } else {
                        currentOnPhotosSelected(results)
                    }
                }
            )
        } else {
            GalleryPickerOrchestrator.launchGallery(
                onPhotoSelected = { result -> 
                    if (cropConfig.enabled) {
                        selectedPhotoForCrop = result
                        showCropView = true
                    } else {
                        currentOnPhotosSelected(listOf(result))
                    }
                },
                onError = currentOnError,
                onDismiss = currentOnDismiss,
                allowMultiple = false,
                selectionLimit = 1,
                compressionLevel = compressionLevel,
                includeExif = includeExif
            )
        }
    }
    
    if (showCropView && selectedPhotoForCrop != null) {
        ImageCropView(
            photoResult = selectedPhotoForCrop!!,
            cropConfig = if (cropConfig.enabled) cropConfig else CropConfig(
                enabled = true,
                circularCrop = false,
                squareCrop = true,
                freeformCrop = true
            ),
            onAccept = { croppedResult ->
                currentOnPhotosSelected(listOf(croppedResult))
                showCropView = false
                selectedPhotoForCrop = null
            },
            onCancel = {
                showCropView = false
                selectedPhotoForCrop = null
                currentOnDismiss()
            }
        )
    }
}
