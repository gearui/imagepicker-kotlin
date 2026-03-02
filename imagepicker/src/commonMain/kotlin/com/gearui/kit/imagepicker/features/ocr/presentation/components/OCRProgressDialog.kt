package com.gearui.kit.imagepicker.features.ocr.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gearui.kit.imagepicker.features.ocr.data.providers.CloudOCRProvider
import com.gearui.kit.imagepicker.features.ocr.model.ExtractionIndicators
import com.gearui.kit.imagepicker.features.ocr.model.OCRProcessState
import com.gearui.kit.imagepicker.domain.utils.getProviderDisplayName
import com.gearui.kit.imagepicker.presentation.ui.components.ocr.OCRProgressContent


@Composable
fun OCRProgressDialog(
    isVisible: Boolean,
    currentState: OCRProcessState,
    provider: CloudOCRProvider? = null,
    providerName: String = getProviderDisplayName(provider),
    extractionIndicators: ExtractionIndicators,
    errorMessage: String? = null,
    onDismiss: () -> Unit = {}
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = { 
                if (currentState == OCRProcessState.SUCCESS || currentState == OCRProcessState.ERROR) {
                    onDismiss()
                }
            },
            properties = DialogProperties(
                dismissOnClickOutside = currentState == OCRProcessState.SUCCESS || currentState == OCRProcessState.ERROR,
                dismissOnBackPress = false
            )
        ) {
            OCRProgressContent(
                currentState = currentState,
                providerName = providerName,
                extractionIndicators = extractionIndicators,
                errorMessage = errorMessage,
                onDismiss = onDismiss
            )
        }
    }
}
