package com.gearui.kit.imagepicker.presentation.ui.components.ocr

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gearui.kit.imagepicker.features.ocr.model.ExtractionIndicators
import com.gearui.kit.imagepicker.features.ocr.model.OCRProcessState

@Composable
 fun OCRProgressContent(
    currentState: OCRProcessState,
    providerName: String,
    extractionIndicators: ExtractionIndicators,
    errorMessage: String?,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentState) {
                OCRProcessState.UPLOADING -> {
                    UploadingContent()
                }
                OCRProcessState.PROCESSING -> {
                    ProcessingContent(providerName, extractionIndicators)
                }
                OCRProcessState.SUCCESS -> {
                    SuccessContent(onDismiss)
                }
                OCRProcessState.ERROR -> {
                    ErrorContent(errorMessage, onDismiss)
                }
            }
        }
    }
}
