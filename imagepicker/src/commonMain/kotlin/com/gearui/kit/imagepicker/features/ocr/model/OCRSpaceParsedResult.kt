package com.gearui.kit.imagepicker.features.ocr.model

import kotlinx.serialization.Serializable

@Serializable
 data class OCRSpaceParsedResult(
    val parsedText: String? = null,
    val fileParseExitCode: String? = null,
    val errorMessage: String? = null,
    val errorDetails: String? = null
)

