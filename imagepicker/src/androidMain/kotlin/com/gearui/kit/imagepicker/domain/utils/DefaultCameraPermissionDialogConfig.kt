package com.gearui.kit.imagepicker.domain.utils
import androidx.compose.runtime.Composable
import com.gearui.kit.imagepicker.domain.config.CameraPermissionDialogConfig
import com.gearui.kit.imagepicker.domain.config.PermissionConfig

@Composable
fun defaultCameraPermissionDialogConfig(): CameraPermissionDialogConfig {
    val defaultConfig = PermissionConfig.createLocalizedComposable()
    return CameraPermissionDialogConfig(
        titleDialogConfig = defaultConfig.titleDialogConfig,
        descriptionDialogConfig = defaultConfig.descriptionDialogConfig,
        btnDialogConfig = defaultConfig.btnDialogConfig,
        titleDialogDenied = defaultConfig.titleDialogDenied,
        descriptionDialogDenied = defaultConfig.descriptionDialogDenied,
        btnDialogDenied = defaultConfig.btnDialogDenied,
        customDeniedDialog = null,
        customSettingsDialog = null
    )
}

