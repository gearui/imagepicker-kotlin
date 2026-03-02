package com.gearui.kit.imagepicker.features.ocr.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.gearui.kit.imagepicker.features.ocr.data.analyzers.CloudOCRAnalyzer
import com.gearui.kit.imagepicker.features.ocr.data.integration.CustomService
import com.gearui.kit.imagepicker.features.ocr.data.providers.CloudOCRProvider
import com.gearui.kit.imagepicker.domain.config.CameraCaptureConfig
import com.gearui.kit.imagepicker.domain.config.GalleryConfig
import com.gearui.kit.imagepicker.domain.config.ImagePickerConfig
import com.gearui.kit.imagepicker.features.ocr.model.ImagePickerOCRConfig
import com.gearui.kit.imagepicker.domain.config.PermissionAndConfirmationConfig
import com.gearui.kit.imagepicker.domain.extensions.loadBytes
import com.gearui.kit.imagepicker.features.ocr.model.OCRProcessState
import com.gearui.kit.imagepicker.features.ocr.model.ScanMode
import com.gearui.kit.imagepicker.presentation.ui.components.ImagePickerLauncher
import com.gearui.kit.imagepicker.features.ocr.presentation.components.OCRProgressDialog
import com.gearui.kit.imagepicker.features.ocr.annotations.ExperimentalOCRApi
import com.gearui.kit.imagepicker.features.ocr.utils.APIKeyValidator
import com.gearui.kit.imagepicker.features.ocr.utils.MissingAPIKeyException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Suppress("FunctionNaming")
@Composable
@ExperimentalOCRApi
actual fun ImagePickerLauncherOCR(
    config: ImagePickerOCRConfig
) {
    LaunchedEffect(config.scanMode) {
        try {
            when (val scanMode = config.scanMode) {
                is ScanMode.Cloud -> {
                    APIKeyValidator.validateProvider(scanMode.provider)
                }
            }
        } catch (e: MissingAPIKeyException) {
            val instructions = when (val scanMode = config.scanMode) {
                is ScanMode.Cloud -> APIKeyValidator.getApiKeyInstructions(scanMode.provider)
            }
            val enhancedMessage = "${e.message}\n\nInstructions: $instructions"
            config.onError(MissingAPIKeyException(enhancedMessage, e))
            return@LaunchedEffect
        } catch (e: Exception) {
            config.onError(e)
            return@LaunchedEffect
        }
    }

    var isProcessingOCR by remember { mutableStateOf(false) }
    var currentOCRState by remember { mutableStateOf(OCRProcessState.UPLOADING) }
    var ocrErrorMessage by remember { mutableStateOf<String?>(null) }

    val ocrProvider = if (config.scanMode is ScanMode.Cloud) config.scanMode.provider else null

    OCRProgressDialog(
        isVisible = isProcessingOCR,
        currentState = currentOCRState,
        provider = ocrProvider,
        extractionIndicators = config.extractionIndicators,
        errorMessage = ocrErrorMessage,
        onDismiss = {
            isProcessingOCR = false
            if (currentOCRState == OCRProcessState.ERROR) {
                config.onCancel()
            }
        }
    )

    val imagePickerConfig = ImagePickerConfig(
        onPhotoCaptured = { photoResult ->
            isProcessingOCR = true
            currentOCRState = OCRProcessState.UPLOADING
            ocrErrorMessage = null

            CoroutineScope(Dispatchers.Default).launch {
                try {
                    delay(800)
                    withContext(Dispatchers.Main) {
                        currentOCRState = OCRProcessState.PROCESSING
                    }
                    val ocrResult = when (config.scanMode) {
                        is ScanMode.Cloud -> {
                            when (val provider = config.scanMode.provider) {
                                is CloudOCRProvider.Gemini -> {
                                    val cloudAnalyzer = CloudOCRAnalyzer(provider.apiKey)
                                    cloudAnalyzer.analyzeImage(photoResult)
                                }

                                is CloudOCRProvider.Custom -> {
                                    val customService = CustomService(provider)
                                    val imageData = photoResult.loadBytes()
                                    customService.extractText(
                                        imageData,
                                        photoResult.mimeType,
                                        photoResult.fileName
                                    )
                                }

                                else -> throw IllegalArgumentException("Provider not supported: $provider")
                            }
                        }
                    }
                    withContext(Dispatchers.Main) {
                        currentOCRState = OCRProcessState.SUCCESS
                        delay(1500)
                        isProcessingOCR = false
                        config.onOCRCompleted(ocrResult)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        currentOCRState = OCRProcessState.ERROR
                        ocrErrorMessage = e.message ?: "Unknown error in ocr analysis"
                    }
                }
            }
        },
        onError = { error ->
            isProcessingOCR = false
            config.onError(error)
        },
        onDismiss = config.onCancel,
        directCameraLaunch = config.directCameraLaunch,
        enableCrop = config.enableCrop,
        dialogTitle = config.dialogTitle,
        takePhotoText = config.takePhotoText,
        selectFromGalleryText = config.selectFromGalleryText,
        cancelText = config.cancelText,
        cameraCaptureConfig = CameraCaptureConfig(
            galleryConfig = GalleryConfig(
                allowMultiple = false,
                mimeTypes = config.allowedMimeTypes,
                selectionLimit = 1
            ),
            permissionAndConfirmationConfig = PermissionAndConfirmationConfig(
                skipConfirmation = true
            )
        )
    )

    ImagePickerLauncher(
        config = imagePickerConfig
    )
}