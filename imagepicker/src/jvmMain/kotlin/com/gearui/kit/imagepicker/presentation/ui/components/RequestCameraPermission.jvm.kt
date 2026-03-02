package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import com.gearui.kit.imagepicker.domain.config.CameraPermissionDialogConfig

/**
 * Desktop implementation of RequestCameraPermission.
 * Camera permissions are not needed on Desktop.
 * This is a no-op implementation.
 */
@Composable
actual fun RequestCameraPermission(
    dialogConfig: CameraPermissionDialogConfig,
    onPermissionPermanentlyDenied: () -> Unit,
    onResult: (Boolean) -> Unit,
    customPermissionHandler: (() -> Unit)?
) {
    // No camera permission required on Desktop
    // Directly call onResult with true to proceed
    onResult(true)
}
