package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import com.gearui.kit.imagepicker.domain.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.domain.models.GalleryPhotoResult
import com.gearui.kit.imagepicker.domain.models.MimeType
import com.gearui.kit.imagepicker.domain.utils.JvmFilePicker

@Suppress("LongParameterList")
@Composable
actual fun GalleryPickerLauncher(
    onPhotosSelected: (List<GalleryPhotoResult>) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit,
    allowMultiple: Boolean,
    mimeTypes: List<MimeType>,
    selectionLimit: Long,
    cameraCaptureConfig: CameraCaptureConfig?,
    enableCrop: Boolean,
    fileFilterDescription: String,
    includeExif: Boolean
) {
    // Use the common JVM file picker utility
    // Note: cameraCaptureConfig and enableCrop are not supported on JVM platform
    JvmFilePicker(
        onPhotosSelected = onPhotosSelected,
        onError = onError,
        onDismiss = onDismiss,
        allowMultiple = allowMultiple,
        mimeTypes = mimeTypes,
        selectionLimit = selectionLimit,
        fileFilterDescription = fileFilterDescription
    )
}