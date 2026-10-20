package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import com.gearui.kit.imagepicker.presentation.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.MimeType
import com.gearui.kit.imagepicker.domain.utils.JsFilePicker

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
    JsFilePicker(
        onPhotosSelected = onPhotosSelected,
        onError = onError,
        onDismiss = onDismiss,
        allowMultiple = allowMultiple,
        mimeTypes = mimeTypes,
        selectionLimit = selectionLimit,
        fileFilterDescription = fileFilterDescription
    )
}
