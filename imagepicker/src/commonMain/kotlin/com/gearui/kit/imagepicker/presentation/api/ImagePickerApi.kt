package com.gearui.kit.imagepicker.presentation.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.gearui.kit.imagepicker.domain.config.CropConfig
import com.gearui.kit.imagepicker.domain.config.GalleryConfig
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.ImagePickerResult
import com.gearui.kit.imagepicker.domain.models.MimeType
import com.gearui.kit.imagepicker.presentation.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.presentation.config.ImagePickerConfig
import com.gearui.kit.imagepicker.presentation.ui.components.ImagePickerLauncher
import imagepicker.imagepicker.generated.resources.Res
import imagepicker.imagepicker.generated.resources.cancel_option
import imagepicker.imagepicker.generated.resources.select_from_gallery_option
import imagepicker.imagepicker.generated.resources.select_option_dialog_title
import imagepicker.imagepicker.generated.resources.take_photo_option
import org.jetbrains.compose.resources.stringResource

enum class ImagePickerSource {
    CameraOrGallery,
    CameraOnly,
    GalleryOnly
}

@Stable
class ImagePickerState internal constructor(initialVisible: Boolean = false) {
    var visible by mutableStateOf(initialVisible)
        private set

    fun show() {
        visible = true
    }

    fun hide() {
        visible = false
    }
}

@Composable
fun rememberImagePickerState(initialVisible: Boolean = false): ImagePickerState {
    return remember { ImagePickerState(initialVisible) }
}

data class ImagePickerSimpleConfig(
    val source: ImagePickerSource = ImagePickerSource.CameraOrGallery,
    val multiple: Boolean = false,
    val selectionLimit: Int = 1,
    val cropEnabled: Boolean = false,
    val includeExif: Boolean = false,
    val mimeTypes: List<MimeType> = listOf(MimeType.IMAGE_ALL),
    val preferCameraWhenAvailable: Boolean = false,
    val dialogTitle: String? = null,
    val takePhotoText: String? = null,
    val selectFromGalleryText: String? = null,
    val cancelText: String? = null
)

data class ImagePickerAdvancedConfig(
    val cameraCaptureConfig: CameraCaptureConfig = CameraCaptureConfig(),
    val cropConfig: CropConfig? = null,
    val customPickerDialog: (@Composable (
        onTakePhoto: () -> Unit,
        onSelectFromGallery: () -> Unit,
        onCancel: () -> Unit
    ) -> Unit)? = null
)

@Composable
fun ImagePicker(
    state: ImagePickerState,
    onResult: (ImagePickerResult) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit = {},
    config: ImagePickerSimpleConfig = ImagePickerSimpleConfig(),
    advanced: ImagePickerAdvancedConfig = ImagePickerAdvancedConfig()
) {
    ImagePickerInternal(
        visible = state.visible,
        onResult = { result ->
            state.hide()
            onResult(result)
        },
        onError = onError,
        onDismiss = {
            state.hide()
            onDismiss()
        },
        config = config,
        advanced = advanced
    )
}

@Composable
private fun ImagePickerInternal(
    visible: Boolean,
    onResult: (ImagePickerResult) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit,
    config: ImagePickerSimpleConfig,
    advanced: ImagePickerAdvancedConfig
) {
    val resolvedAllowMultiple = config.multiple || config.selectionLimit > 1
    val resolvedSelectionLimit = if (resolvedAllowMultiple) {
        config.selectionLimit.coerceAtLeast(2)
    } else {
        1
    }

    val resolvedCropConfig = advanced.cropConfig ?: CropConfig(enabled = config.cropEnabled)
    val resolvedGalleryConfig = GalleryConfig(
        allowMultiple = resolvedAllowMultiple,
        mimeTypes = config.mimeTypes,
        selectionLimit = resolvedSelectionLimit,
        includeExif = config.includeExif
    )

    val dialogTitle = config.dialogTitle ?: stringResource(Res.string.select_option_dialog_title)
    val takePhotoText = config.takePhotoText ?: stringResource(Res.string.take_photo_option)
    val selectFromGalleryText = config.selectFromGalleryText ?: stringResource(Res.string.select_from_gallery_option)
    val cancelText = config.cancelText ?: stringResource(Res.string.cancel_option)

    ImagePickerLauncher(
        visible = visible,
        config = ImagePickerConfig(
            onResult = onResult,
            onError = onError,
            onDismiss = onDismiss,
            dialogTitle = dialogTitle,
            takePhotoText = takePhotoText,
            selectFromGalleryText = selectFromGalleryText,
            cancelText = cancelText,
            customPickerDialog = advanced.customPickerDialog,
            cameraCaptureConfig = advanced.cameraCaptureConfig.copy(
                includeExif = advanced.cameraCaptureConfig.includeExif || config.includeExif
            ),
            galleryConfig = resolvedGalleryConfig,
            cropConfig = resolvedCropConfig,
            directCameraLaunch = config.source == ImagePickerSource.CameraOnly,
            directGalleryLaunch = config.source == ImagePickerSource.GalleryOnly,
            preferCameraWhenAvailable = config.preferCameraWhenAvailable
        )
    )
}

@Composable
fun PickSingleImage(
    state: ImagePickerState,
    onPicked: (ImageAsset) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit = {},
    source: ImagePickerSource = ImagePickerSource.CameraOrGallery,
    cropEnabled: Boolean = false,
    includeExif: Boolean = false,
    mimeTypes: List<MimeType> = listOf(MimeType.IMAGE_ALL),
    preferCameraWhenAvailable: Boolean = false
) {
    PickSingleImageInternal(
        visible = state.visible,
        onPicked = { asset ->
            state.hide()
            onPicked(asset)
        },
        onError = onError,
        onDismiss = {
            state.hide()
            onDismiss()
        },
        source = source,
        cropEnabled = cropEnabled,
        includeExif = includeExif,
        mimeTypes = mimeTypes,
        preferCameraWhenAvailable = preferCameraWhenAvailable
    )
}

@Composable
private fun PickSingleImageInternal(
    visible: Boolean,
    onPicked: (ImageAsset) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit,
    source: ImagePickerSource,
    cropEnabled: Boolean,
    includeExif: Boolean,
    mimeTypes: List<MimeType>,
    preferCameraWhenAvailable: Boolean
) {
    ImagePickerInternal(
        visible = visible,
        onError = onError,
        onDismiss = onDismiss,
        onResult = { result ->
            when (result) {
                is ImagePickerResult.Single -> onPicked(result.asset)
                is ImagePickerResult.Multiple -> {
                    result.assets.firstOrNull()?.let(onPicked) ?: onDismiss()
                }
            }
        },
        config = ImagePickerSimpleConfig(
            source = source,
            multiple = false,
            selectionLimit = 1,
            cropEnabled = cropEnabled,
            includeExif = includeExif,
            mimeTypes = mimeTypes,
            preferCameraWhenAvailable = preferCameraWhenAvailable
        ),
        advanced = ImagePickerAdvancedConfig()
    )
}

@Composable
fun PickImages(
    state: ImagePickerState,
    onPicked: (List<ImageAsset>) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit = {},
    selectionLimit: Int = 9,
    includeExif: Boolean = false,
    mimeTypes: List<MimeType> = listOf(MimeType.IMAGE_ALL)
) {
    PickImagesInternal(
        visible = state.visible,
        onPicked = { assets ->
            state.hide()
            onPicked(assets)
        },
        onError = onError,
        onDismiss = {
            state.hide()
            onDismiss()
        },
        selectionLimit = selectionLimit,
        includeExif = includeExif,
        mimeTypes = mimeTypes
    )
}

@Composable
private fun PickImagesInternal(
    visible: Boolean,
    onPicked: (List<ImageAsset>) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit,
    selectionLimit: Int,
    includeExif: Boolean,
    mimeTypes: List<MimeType>
) {
    ImagePickerInternal(
        visible = visible,
        onError = onError,
        onDismiss = onDismiss,
        onResult = { result ->
            when (result) {
                is ImagePickerResult.Single -> onPicked(listOf(result.asset))
                is ImagePickerResult.Multiple -> onPicked(result.assets)
            }
        },
        config = ImagePickerSimpleConfig(
            source = ImagePickerSource.GalleryOnly,
            multiple = true,
            selectionLimit = selectionLimit,
            cropEnabled = false,
            includeExif = includeExif,
            mimeTypes = mimeTypes
        ),
        advanced = ImagePickerAdvancedConfig()
    )
}

@Composable
fun rememberSingleImagePickerLauncher(
    onPicked: (ImageAsset) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit = {},
    source: ImagePickerSource = ImagePickerSource.CameraOrGallery,
    cropEnabled: Boolean = false,
    includeExif: Boolean = false,
    mimeTypes: List<MimeType> = listOf(MimeType.IMAGE_ALL),
    preferCameraWhenAvailable: Boolean = false
): () -> Unit {
    val state = rememberImagePickerState()
    PickSingleImage(
        state = state,
        onPicked = onPicked,
        onError = onError,
        onDismiss = onDismiss,
        source = source,
        cropEnabled = cropEnabled,
        includeExif = includeExif,
        mimeTypes = mimeTypes,
        preferCameraWhenAvailable = preferCameraWhenAvailable
    )
    return remember { { state.show() } }
}

@Composable
fun rememberMultiImagePickerLauncher(
    onPicked: (List<ImageAsset>) -> Unit,
    onError: (Exception) -> Unit,
    onDismiss: () -> Unit = {},
    selectionLimit: Int = 9,
    includeExif: Boolean = false,
    mimeTypes: List<MimeType> = listOf(MimeType.IMAGE_ALL)
): () -> Unit {
    val state = rememberImagePickerState()
    PickImages(
        state = state,
        onPicked = onPicked,
        onError = onError,
        onDismiss = onDismiss,
        selectionLimit = selectionLimit,
        includeExif = includeExif,
        mimeTypes = mimeTypes
    )
    return remember { { state.show() } }
}
