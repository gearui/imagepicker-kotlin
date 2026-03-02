package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import com.gearui.kit.imagepicker.domain.config.ImagePickerConfig
import com.gearui.kit.imagepicker.domain.models.GalleryPhotoResult
import com.gearui.kit.imagepicker.domain.models.MimeType
import com.gearui.kit.imagepicker.domain.models.PhotoResult
import com.gearui.kit.imagepicker.domain.utils.JvmFilePicker

@Suppress("FunctionNaming")
@Composable
actual fun ImagePickerLauncher(
    config: ImagePickerConfig
) {
    // On JVM, we can only access the file gallery
    // No camera is available, so we launch the file selector directly
    
    JvmFilePicker(
        onPhotosSelected = { galleryResults ->
            if (galleryResults.size > 1 && config.onPhotosSelected != null) {
                config.onPhotosSelected.invoke(galleryResults)
            } else if (galleryResults.size == 1) {
                val galleryResult = galleryResults.first()
                val photoResult = PhotoResult(
                    uri = galleryResult.uri,
                    width = galleryResult.width,
                    height = galleryResult.height,
                    fileName = galleryResult.fileName,
                    fileSize = galleryResult.fileSize
                )
                config.onPhotoCaptured(photoResult)
            } else {
                config.onDismiss()
            }
        },
        onError = { exception ->
            config.onError(exception)
        },
        onDismiss = {
            config.onDismiss()
        },
        allowMultiple = config.onPhotosSelected != null,
        mimeTypes = listOf(MimeType.IMAGE_JPEG, MimeType.IMAGE_PNG),
        selectionLimit = 30,
        fileFilterDescription = "Images"
    )
}
