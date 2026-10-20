package com.gearui.kit.imagepicker.domain.extensions

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.BytesPolicy
import org.jetbrains.skia.Image
import java.io.File
import java.util.Base64

actual suspend fun ImageAsset.readBytes(policy: BytesPolicy): ByteArray {
    return try {
        val file = File(java.net.URI(uri))
        file.readBytes()
    } catch (_: Exception) {
        byteArrayOf()
    }
}

actual fun ImageAsset.loadBase64(): String {
    return try {
        val bytes = kotlinx.coroutines.runBlocking { readBytes(BytesPolicy.ORIGINAL) }
        if (bytes.isNotEmpty()) Base64.getEncoder().encodeToString(bytes) else ""
    } catch (_: Exception) {
        ""
    }
}

actual fun ImageAsset.loadImageBitmap(): ImageBitmap? {
    return try {
        val bytes = kotlinx.coroutines.runBlocking { readBytes(BytesPolicy.ORIGINAL) }
        if (bytes.isNotEmpty()) Image.makeFromEncoded(bytes).toComposeImageBitmap() else null
    } catch (_: Exception) {
        null
    }
}

actual fun ImageAsset.loadPainter(): Painter? {
    return loadImageBitmap()?.let { BitmapPainter(it) }
}
