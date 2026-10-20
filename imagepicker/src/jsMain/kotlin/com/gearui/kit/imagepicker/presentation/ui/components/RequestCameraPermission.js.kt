package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gearui.kit.imagepicker.presentation.config.CameraPermissionDialogConfig

@Composable
actual fun RequestCameraPermission(
    dialogConfig: CameraPermissionDialogConfig,
    onPermissionPermanentlyDenied: () -> Unit,
    onResult: (Boolean) -> Unit,
    customPermissionHandler: (() -> Unit)?
) {
    LaunchedEffect(dialogConfig) {
        onResult(true)
    }
}
