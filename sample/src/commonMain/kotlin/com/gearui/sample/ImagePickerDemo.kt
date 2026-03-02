package com.gearui.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.gearui.GearApp
import com.gearui.View
import com.gearui.components.actionsheet.ActionSheet
import com.gearui.components.actionsheet.ActionSheetAlign
import com.gearui.components.actionsheet.ActionSheetItem
import com.gearui.components.button.Button
import com.gearui.components.navbar.NavBar
import com.gearui.components.toast.Toast
import com.gearui.foundation.primitives.Text
import com.gearui.foundation.typography.Typography
import com.gearui.theme.Theme
import com.tencent.kuikly.compose.foundation.layout.Arrangement
import com.tencent.kuikly.compose.foundation.layout.Column
import com.tencent.kuikly.compose.foundation.layout.Spacer
import com.tencent.kuikly.compose.foundation.layout.fillMaxSize
import com.tencent.kuikly.compose.foundation.layout.height
import com.tencent.kuikly.compose.foundation.layout.padding
import com.tencent.kuikly.compose.ui.Modifier
import com.tencent.kuikly.compose.ui.unit.dp
import com.tencent.kuikly.core.annotations.Page

@Page("ImagePickerDemo")
class ImagePickerDemo : View() {
    @Composable
    override fun Content() {
        var showLauncher by remember { mutableStateOf(false) }
        var lastResult by remember { mutableStateOf("No image selected") }
        val colors = Theme.colors

        GearApp {
            Column(modifier = Modifier.fillMaxSize()) {
                NavBar(
                    title = "ImagePicker Sample",
                    centerTitle = true,
                    useDefaultBack = true
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "This is the sample entry of ImagePicker in gearui mode.",
                        style = Typography.BodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        text = "Open Image Picker",
                        onClick = { showLauncher = true }
                    )
                    Text(
                        text = "Last result: $lastResult",
                        style = Typography.BodySmall,
                        color = colors.textSecondary
                    )
                }
            }

            if (showLauncher) {
                ActionSheet.showList(
                    items = listOf(
                        ActionSheetItem(label = "Take photo"),
                        ActionSheetItem(label = "Choose from gallery")
                    ),
                    description = "Select image source",
                    align = ActionSheetAlign.CENTER,
                    showCancel = true,
                    cancelText = "Cancel",
                    onSelected = { _, index ->
                        showLauncher = false
                        val source = when (index) {
                            0 -> ImagePickerSource.CAMERA
                            else -> ImagePickerSource.GALLERY
                        }
                        NativeImagePickerBridge.open(
                            source = source,
                            onPicked = { uri ->
                                lastResult = uri
                                Toast.show("Image selected")
                            },
                            onError = { message ->
                                Toast.show(message)
                            },
                            onCancel = {
                                Toast.show("Cancelled")
                            }
                        )
                    },
                    onCancel = {
                        showLauncher = false
                    }
                )
            }

            ActionSheet.Host()
        }
    }
}

enum class ImagePickerSource {
    CAMERA,
    GALLERY
}

expect object NativeImagePickerBridge {
    fun open(
        source: ImagePickerSource,
        onPicked: (uri: String) -> Unit,
        onError: (message: String) -> Unit,
        onCancel: () -> Unit
    )
}
