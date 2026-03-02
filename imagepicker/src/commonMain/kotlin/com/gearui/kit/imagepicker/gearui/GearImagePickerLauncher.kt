package com.gearui.kit.imagepicker.gearui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gearui.components.actionsheet.ActionSheet
import com.gearui.components.actionsheet.ActionSheetAlign
import com.gearui.components.actionsheet.ActionSheetItem

/**
 * GearUI ActionSheet-based launcher.
 *
 * This launcher provides the GearUI interaction shell and delegates concrete
 * camera/gallery execution to host callbacks.
 */
@Composable
fun GearImagePickerLauncher(
    visible: Boolean,
    config: GearImagePickerConfig
) {
    LaunchedEffect(visible) {
        if (visible) {
            ActionSheet.showList(
                items = listOf(
                    ActionSheetItem(label = config.takePhotoText),
                    ActionSheetItem(label = config.selectFromGalleryText)
                ),
                description = config.title,
                align = ActionSheetAlign.CENTER,
                showCancel = true,
                cancelText = config.cancelText,
                onSelected = { _, index ->
                    when (index) {
                        0 -> config.onTakePhoto()
                        1 -> config.onSelectFromGallery()
                    }
                },
                onCancel = { config.onCancel?.invoke() }
            )
        } else {
            ActionSheet.dismiss()
        }
    }

    ActionSheet.Host()
}

