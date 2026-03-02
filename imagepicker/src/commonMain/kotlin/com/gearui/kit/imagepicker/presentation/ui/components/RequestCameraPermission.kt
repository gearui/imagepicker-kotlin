package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import com.gearui.kit.imagepicker.domain.config.CameraPermissionDialogConfig

@Composable
expect fun RequestCameraPermission(
    dialogConfig: CameraPermissionDialogConfig,
    onPermissionPermanentlyDenied: () -> Unit,
    onResult: (Boolean) -> Unit,
    customPermissionHandler: (() -> Unit)?
)
