package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gearui.kit.imagepicker.domain.exceptions.ImagePickerErrorCode
import com.gearui.kit.imagepicker.domain.exceptions.UnsupportedPlatformException
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.ImagePickerResult
import com.gearui.kit.imagepicker.domain.models.MimeType
import com.gearui.kit.imagepicker.domain.utils.JvmFilePicker
import com.gearui.kit.imagepicker.presentation.config.ImagePickerConfig

@Suppress("FunctionNaming")
@Composable
actual fun ImagePickerLauncher(
    config: ImagePickerConfig,
    visible: Boolean
) {
    if (!visible) {
        return
    }

    if (config.directCameraLaunch) {
        LaunchedEffect(visible) {
            config.onError(
                UnsupportedPlatformException(
                    code = ImagePickerErrorCode.CAMERA_NOT_SUPPORTED,
                    message = "Camera capture is not supported on JVM desktop"
                )
            )
            config.onDismiss()
        }
        return
    }

    JvmFilePicker(
        onPhotosSelected = { galleryResults ->
            if (galleryResults.size > 1) {
                config.onResult(ImagePickerResult.Multiple(galleryResults))
            } else if (galleryResults.size == 1) {
                val galleryResult = galleryResults.first()
                val photoResult = ImageAsset(
                    uri = galleryResult.uri,
                    width = galleryResult.width,
                    height = galleryResult.height,
                    fileName = galleryResult.fileName,
                    fileSize = galleryResult.fileSize
                )
                config.onResult(ImagePickerResult.Single(photoResult))
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
        allowMultiple = config.galleryConfig.allowMultiple,
        mimeTypes = if (config.galleryConfig.mimeTypes.isEmpty()) {
            listOf(MimeType.IMAGE_JPEG, MimeType.IMAGE_PNG)
        } else {
            config.galleryConfig.mimeTypes
        },
        selectionLimit = config.galleryConfig.selectionLimit.toLong(),
        fileFilterDescription = "Images"
    )
}
