package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gearui.kit.imagepicker.domain.exceptions.ImagePickerErrorCode
import com.gearui.kit.imagepicker.domain.exceptions.UnsupportedPlatformException
import com.gearui.kit.imagepicker.presentation.config.ImagePickerConfig

@Composable
actual fun ImagePickerLauncher(
    config: ImagePickerConfig,
    visible: Boolean
) {
    if (!visible) return

    LaunchedEffect(visible) {
        config.onError(
            UnsupportedPlatformException(
                code = ImagePickerErrorCode.PLATFORM_NOT_SUPPORTED,
                message = "ImagePicker is not supported on WASM yet"
            )
        )
        config.onDismiss()
    }
}
