package com.gearui.kit.imagepicker.features.ocr.data.analyzers

import com.gearui.kit.imagepicker.features.ocr.model.OCRResult
import com.gearui.kit.imagepicker.domain.models.PhotoResult

/**
 * Interface for OCR analysis implementations
 * Provides a contract for both local and cloud-based text recognition
 */
interface OCRAnalyzer {
    suspend fun analyzeImage(photoResult: PhotoResult): OCRResult
}
