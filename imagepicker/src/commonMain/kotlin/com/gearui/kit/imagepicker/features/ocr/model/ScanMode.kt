package com.gearui.kit.imagepicker.features.ocr.model

import com.gearui.kit.imagepicker.features.ocr.data.providers.CloudOCRProvider

/**
 * Scan modes for OCR functionality
 */
sealed class ScanMode {
    data class Cloud(
        val provider: CloudOCRProvider,
        val config: OCRRequestConfig = OCRRequestConfig()
    ) : ScanMode()
}