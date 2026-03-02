package com.gearui.kit.imagepicker.features.ocr.data.providers

import com.gearui.kit.imagepicker.features.ocr.model.OCRResult
import com.gearui.kit.imagepicker.features.ocr.model.OCRRequestConfig
import com.gearui.kit.imagepicker.features.ocr.model.ProviderInfo

interface CloudOCRProviderInterface {
    val providerName: String
    suspend fun extractText(
        imageData: ByteArray, 
        config: OCRRequestConfig = OCRRequestConfig()
    ): OCRResult

    suspend fun isAvailable(): Boolean
    fun getProviderInfo(): ProviderInfo

    fun close()
}
