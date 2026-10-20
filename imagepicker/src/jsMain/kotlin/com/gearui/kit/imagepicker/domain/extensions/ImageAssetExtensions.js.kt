package com.gearui.kit.imagepicker.domain.extensions

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.BytesPolicy
import org.jetbrains.skia.Image
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
actual suspend fun ImageAsset.readBytes(policy: BytesPolicy): ByteArray {
    return decodeBytesFromDataUrl(uri)
}

@OptIn(ExperimentalEncodingApi::class)
private fun decodeBytesFromDataUrl(uri: String): ByteArray {
    return try {
        val dataUrlPrefix = "data:"
        val base64Prefix = ";base64,"
        if (uri.startsWith(dataUrlPrefix) && uri.contains(base64Prefix)) {
            Base64.decode(uri.substringAfter(base64Prefix))
        } else {
            byteArrayOf()
        }
    } catch (_: Exception) {
        byteArrayOf()
    }
}

actual fun ImageAsset.loadBase64(): String {
    return try {
        val dataUrlPrefix = "data:"
        val base64Prefix = ";base64,"
        if (uri.startsWith(dataUrlPrefix) && uri.contains(base64Prefix)) {
            uri.substringAfter(base64Prefix)
        } else {
            ""
        }
    } catch (_: Exception) {
        ""
    }
}

actual fun ImageAsset.loadImageBitmap(): ImageBitmap? {
    return try {
        val bytes = decodeBytesFromDataUrl(uri)
        if (bytes.isNotEmpty()) Image.makeFromEncoded(bytes).toComposeImageBitmap() else null
    } catch (_: Exception) {
        null
    }
}

actual fun ImageAsset.loadPainter(): Painter? {
    return loadImageBitmap()?.let { BitmapPainter(it) }
}
