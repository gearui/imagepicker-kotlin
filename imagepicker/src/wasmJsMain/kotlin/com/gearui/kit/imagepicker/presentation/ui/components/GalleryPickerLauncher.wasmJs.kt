package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.domain.exceptions.ImagePickerErrorCode
import com.gearui.kit.imagepicker.domain.exceptions.UnsupportedPlatformException
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.MimeType
import com.gearui.kit.imagepicker.presentation.config.CameraCaptureConfig

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
    LaunchedEffect(Unit) {
        onError(
            UnsupportedPlatformException(
                code = ImagePickerErrorCode.GALLERY_NOT_SUPPORTED,
                message = "Gallery picker is not supported on WASM yet"
            )
        )
        onDismiss()
    }
}
