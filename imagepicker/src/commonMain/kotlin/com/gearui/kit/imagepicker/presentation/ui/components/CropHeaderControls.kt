package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearui.components.button.Button
import com.gearui.components.button.ButtonShape
import com.gearui.components.button.ButtonSize
import com.gearui.components.button.ButtonTheme
import com.gearui.components.button.ButtonType
import com.gearui.components.icon.Icons
import imagepicker.imagepicker.generated.resources.Res
import imagepicker.imagepicker.generated.resources.image_crop_view_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun CropHeaderControls(
    onCancel: () -> Unit,
    applyCrop: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A))
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.align(Alignment.CenterStart)) {
            Button(
                text = "",
                onClick = onCancel,
                icon = Icons.close,
                theme = ButtonTheme.DEFAULT,
                type = ButtonType.GHOST,
                size = ButtonSize.SMALL,
                shape = ButtonShape.CIRCLE
            )
        }

        Text(
            text = stringResource(Res.string.image_crop_view_title),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            Button(
                text = "",
                onClick = applyCrop,
                icon = Icons.done,
                theme = ButtonTheme.PRIMARY,
                type = ButtonType.FILL,
                size = ButtonSize.SMALL,
                shape = ButtonShape.CIRCLE
            )
        }
    }
}
