package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import com.gearui.kit.imagepicker.presentation.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.presentation.config.ImagePickerUiConstants.SELECTION_LIMIT
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.MimeType

@Suppress("LongParameterList")
@Composable
expect fun GalleryPickerLauncher(
    onPhotosSelected: (List<ImageAsset>) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit = {},
    allowMultiple: Boolean = false,
    mimeTypes: List<MimeType> = listOf(MimeType.IMAGE_ALL),
    selectionLimit: Long = SELECTION_LIMIT,
    cameraCaptureConfig: CameraCaptureConfig? = null,
    cropConfig: CropConfig = CropConfig(),
    fileFilterDescription: String = "Image files",
    includeExif: Boolean = false
)
