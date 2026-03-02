package com.gearui.kit.imagepicker.domain.utils

import com.gearui.kit.imagepicker.features.ocr.data.providers.CloudOCRProvider

fun getProviderDisplayName(provider: CloudOCRProvider?): String {
    return when (provider) {
        is CloudOCRProvider.Gemini -> "Gemini"
        is CloudOCRProvider.Ollama -> "Ollama"
        is CloudOCRProvider.OpenAI -> "OpenAI"
        is CloudOCRProvider.Claude -> "Claude"
        is CloudOCRProvider.AzureVision -> "Azure Vision"
        is CloudOCRProvider.Custom -> provider.name
        null -> "AI"
    }
}