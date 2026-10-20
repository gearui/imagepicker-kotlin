package com.gearui.kit.imagepicker.presentation.config

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import imagepicker.imagepicker.generated.resources.Res
import imagepicker.imagepicker.generated.resources.camera_permission_denied
import imagepicker.imagepicker.generated.resources.camera_permission_denied_description
import imagepicker.imagepicker.generated.resources.camera_permission_description
import imagepicker.imagepicker.generated.resources.camera_permission_required
import imagepicker.imagepicker.generated.resources.grant_permission
import imagepicker.imagepicker.generated.resources.open_settings

/**
 * Configuration for camera permission dialogs and messages.
 *
 * Provides customizable titles, descriptions, and button texts for permission request and denial dialogs.
 */
data class PermissionConfig(
    val titleDialogConfig: String = "Camera permission required",
    val descriptionDialogConfig: String = "Camera permission is required " +
            "to capture photos. Please grant it in settings",
    val btnDialogConfig: String = "Open settings",
    val titleDialogDenied: String = "Camera permission denied",
    val descriptionDialogDenied: String = "Camera permission is " +
            "required to capture photos. Please grant the permissions",
    val btnDialogDenied: String = "Grant permission"
) {
    /**
     * Companion object providing a method to create a localized [PermissionConfig] using Compose.
     */
    companion object {
        @Composable
        fun createLocalizedComposable(): PermissionConfig {
            return PermissionConfig(
                titleDialogConfig = stringResource(Res.string.camera_permission_required),
                descriptionDialogConfig = stringResource(Res.string.camera_permission_description),
                btnDialogConfig = stringResource(Res.string.open_settings),
                titleDialogDenied = stringResource(Res.string.camera_permission_denied),
                descriptionDialogDenied = stringResource(Res.string.camera_permission_denied_description),
                btnDialogDenied = stringResource(Res.string.grant_permission)
            )
        }
    }
}
