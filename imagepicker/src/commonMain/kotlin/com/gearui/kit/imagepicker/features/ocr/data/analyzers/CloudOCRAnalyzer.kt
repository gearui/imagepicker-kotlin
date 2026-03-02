package com.gearui.kit.imagepicker.features.ocr.data.analyzers

import com.gearui.kit.imagepicker.features.ocr.utils.CloudOCRException
import com.gearui.kit.imagepicker.features.ocr.utils.InvalidAPIKeyException
import com.gearui.kit.imagepicker.features.ocr.utils.MissingAPIKeyException
import com.gearui.kit.imagepicker.features.ocr.model.OCRResult
import com.gearui.kit.imagepicker.domain.models.PhotoResult
import com.gearui.kit.imagepicker.domain.extensions.loadBytes
import com.gearui.kit.imagepicker.domain.utils.OCRUtils
import com.gearui.kit.imagepicker.features.ocr.data.providers.GeminiOCRProvider
import com.gearui.kit.imagepicker.features.ocr.model.OCRRequestConfig

/**
 * Cloud OCR analyzer implementation
 * Uses Google Gemini API for advanced text recognition
 * Common implementation that works across all platforms using Ktor
 */
class CloudOCRAnalyzer(apiKey: String) : OCRAnalyzer {

    val provider = GeminiOCRProvider(apiKey)

    init {
        if (apiKey.isBlank()) {
            throw MissingAPIKeyException.emptyApiKey("Google Gemini")
        }
    }
    
    override suspend fun analyzeImage(photoResult: PhotoResult): OCRResult {
        val imageData = photoResult.loadBytes()
        val mimeType = OCRUtils.detectMimeType(imageData, null)
        
        return try {
            val timeoutMs = OCRUtils.calculateTimeout(imageData.size, mimeType)
            if (timeoutMs > 60000) {
                val config = OCRRequestConfig(
                    mimeType = mimeType,
                    timeout = timeoutMs,
                    maxTokens = OCRUtils.calculateMaxTokens(imageData.size, mimeType)
                )
                provider.extractText(imageData, config)
            } else {
                provider.extractText(imageData, OCRRequestConfig(mimeType = mimeType, timeout = timeoutMs))
            }
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("timeout", ignoreCase = true) == true -> 
                    "Request timed out. File size: ${imageData.size / 1024}KB, Type: $mimeType. Consider reducing file size or try again."
                e.message?.contains("socket", ignoreCase = true) == true -> 
                    "Network connection issue. File size: ${imageData.size / 1024}KB, Type: $mimeType. Please check your connection and try again."
                e.message?.contains("content-length", ignoreCase = true) == true ->
                    "Network response error (content-length mismatch). File size: ${imageData.size / 1024}KB, Type: $mimeType. This is a known issue with some network configurations. Please try again."
                else -> "Cloud OCR analysis failed: ${e.message}"
            }
            throw CloudOCRException(errorMessage, e)
        }
    }
}
