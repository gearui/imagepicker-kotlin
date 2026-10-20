package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import com.gearui.kit.imagepicker.presentation.config.ImagePickerConfig

@Suppress("FunctionNaming")
@Composable
expect fun ImagePickerLauncher(
    config: ImagePickerConfig,
    visible: Boolean = true
)
