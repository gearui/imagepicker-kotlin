package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.gearui.kit.imagepicker.domain.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.domain.models.GalleryPhotoResult
import com.gearui.kit.imagepicker.domain.models.GalleryPickerConfig
import com.gearui.kit.imagepicker.domain.models.MimeType
import com.gearui.kit.imagepicker.presentation.ui.components.gallery.GalleryPickerLauncherContent
import imagepicker.imagepicker.generated.resources.Res
import imagepicker.imagepicker.generated.resources.invalid_context_error
import org.jetbrains.compose.resources.stringResource

@Suppress("ReturnCount","LongParameterList")
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
    val context = LocalContext.current
    val activity = context
    val invalidContextMsg = stringResource(Res.string.invalid_context_error)
    if (activity !is ComponentActivity) {
        onError(Exception(invalidContextMsg))
        return
    }

    val config = GalleryPickerConfig(
        context = context,
        onPhotosSelected = onPhotosSelected,
        onError = onError,
        onDismiss = onDismiss,
        allowMultiple = allowMultiple,
        mimeTypes = mimeTypes.map { it.value },
        cameraCaptureConfig = cameraCaptureConfig,
        enableCrop = enableCrop,
        includeExif = includeExif
    )
    GalleryPickerLauncherContent(config)
}