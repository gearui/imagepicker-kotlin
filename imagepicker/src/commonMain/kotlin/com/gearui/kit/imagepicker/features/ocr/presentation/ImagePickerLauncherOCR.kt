package com.gearui.kit.imagepicker.features.ocr.presentation

import androidx.compose.runtime.Composable
import com.gearui.kit.imagepicker.features.ocr.model.ImagePickerOCRConfig
import com.gearui.kit.imagepicker.features.ocr.annotations.ExperimentalOCRApi


@Suppress("FunctionNaming")
@Composable
@ExperimentalOCRApi
expect fun ImagePickerLauncherOCR(
    config: ImagePickerOCRConfig
)
