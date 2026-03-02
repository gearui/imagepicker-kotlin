package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import com.gearui.kit.imagepicker.domain.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.domain.config.ImagePickerUiConstants.SELECTION_LIMIT
import com.gearui.kit.imagepicker.domain.models.GalleryPhotoResult
import com.gearui.kit.imagepicker.domain.models.MimeType

@Suppress("LongParameterList")
@Composable
expect fun GalleryPickerLauncher(
    onPhotosSelected: (List<GalleryPhotoResult>) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit = {},
    allowMultiple: Boolean = false,
    mimeTypes: List<MimeType> = listOf(MimeType.IMAGE_ALL),
    selectionLimit: Long = SELECTION_LIMIT,
    cameraCaptureConfig: CameraCaptureConfig? = null,
    enableCrop: Boolean = false,
    fileFilterDescription: String = "Image files",
    includeExif: Boolean = false
)

