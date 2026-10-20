package com.gearui.kit.imagepicker.domain.extensions

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.BytesPolicy

actual suspend fun ImageAsset.readBytes(policy: BytesPolicy): ByteArray = ByteArray(0)

actual fun ImageAsset.loadBase64(): String = ""

actual fun ImageAsset.loadImageBitmap(): ImageBitmap? = null

actual fun ImageAsset.loadPainter(): Painter? = null
