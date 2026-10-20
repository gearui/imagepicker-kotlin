package com.gearui.kit.imagepicker.presentation.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.domain.config.GalleryConfig
import com.gearui.kit.imagepicker.domain.models.CapturePhotoPreference
import com.gearui.kit.imagepicker.domain.models.CompressionLevel
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.ImagePickerResult

/**
 * Presentation-layer UI styling config.
 */
data class UiConfig(
    val buttonColor: Color? = null,
    val iconColor: Color? = null,
    val buttonSize: Dp? = null,
    val flashIcon: ImageVector? = null,
    val switchCameraIcon: ImageVector? = null,
    val galleryIcon: ImageVector? = null
)

data class CameraCallbacks(
    val onCameraReady: (() -> Unit)? = null,
    val onCameraSwitch: (() -> Unit)? = null,
    val onPermissionError: ((Exception) -> Unit)? = null,
    val onGalleryOpened: (() -> Unit)? = null
)

data class PermissionAndConfirmationConfig(
    val customPermissionHandler: ((PermissionConfig) -> Unit)? = null,
    val customConfirmationView: (@Composable (ImageAsset, (ImageAsset) -> Unit, () -> Unit) -> Unit)? = null,
    val customDeniedDialog: (@Composable ((onRetry: () -> Unit) -> Unit))? = null,
    val customSettingsDialog: (@Composable ((onOpenSettings: () -> Unit) -> Unit))? = null,
    val skipConfirmation: Boolean = false,
    val cancelButtonTextIOS: String? = "Cancel",
    val onCancelPermissionConfigIOS: (() -> Unit)? = null
)

data class CameraCaptureConfig(
    val preference: CapturePhotoPreference = CapturePhotoPreference.BALANCED,
    val captureButtonSize: Dp = 72.dp,
    val compressionLevel: CompressionLevel? = CompressionLevel.MEDIUM,
    val includeExif: Boolean = false,
    val uiConfig: UiConfig = UiConfig(),
    val cameraCallbacks: CameraCallbacks = CameraCallbacks(),
    val permissionAndConfirmationConfig: PermissionAndConfirmationConfig = PermissionAndConfirmationConfig()
)

data class ImagePickerConfig(
    val onResult: (ImagePickerResult) -> Unit,
    val onError: (Exception) -> Unit,
    val onDismiss: () -> Unit = {},
    val dialogTitle: String = "Select option",
    val takePhotoText: String = "Take photo",
    val selectFromGalleryText: String = "Select from gallery",
    val cancelText: String = "Cancel",
    val customPickerDialog: (
        @Composable (
            onTakePhoto: () -> Unit,
            onSelectFromGallery: () -> Unit,
            onCancel: () -> Unit
        ) -> Unit
    )? = null,
    val cameraCaptureConfig: CameraCaptureConfig = CameraCaptureConfig(),
    val galleryConfig: GalleryConfig = GalleryConfig(),
    val cropConfig: CropConfig = CropConfig(),
    val directCameraLaunch: Boolean = false,
    val directGalleryLaunch: Boolean = false,
    val preferCameraWhenAvailable: Boolean = false
)

data class CameraPreviewConfig(
    val captureButtonSize: Dp = 72.dp,
    val uiConfig: UiConfig = UiConfig(),
    val cameraCallbacks: CameraCallbacks = CameraCallbacks(),
    val galleryConfig: GalleryConfig = GalleryConfig()
)

data class CameraPermissionDialogConfig(
    val titleDialogConfig: String,
    val descriptionDialogConfig: String,
    val btnDialogConfig: String,
    val titleDialogDenied: String,
    val descriptionDialogDenied: String,
    val btnDialogDenied: String,
    val customDeniedDialog: @Composable ((onRetry: () -> Unit) -> Unit)? = null,
    val customSettingsDialog: @Composable ((onOpenSettings: () -> Unit) -> Unit)? = null,
    val cancelButtonText: String? = "Cancel",
    val onCancelPermissionConfigIOS: (() -> Unit)? = null
)
