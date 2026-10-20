package com.gearui.kit.imagepicker.domain.extensions

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import com.gearui.kit.imagepicker.domain.models.BytesPolicy
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import kotlinx.coroutines.runBlocking

/**
 * Reads image bytes with configurable quality policy.
 */
expect suspend fun ImageAsset.readBytes(policy: BytesPolicy = BytesPolicy.ORIGINAL): ByteArray

/**
 * Backward-compatible sync API.
 */
fun ImageAsset.loadBytes(): ByteArray = runBlocking {
    readBytes(BytesPolicy.OPTIMIZED)
}

/**
 * Extension function to load the photo data as Base64 encoded string from the URI.
 * @return Base64 encoded string containing the image data, or empty string if loading fails.
 */
expect fun ImageAsset.loadBase64(): String

/**
 * Extension function to load the photo as ImageBitmap for Compose usage.
 * Perfect for use with Painter or Canvas in Compose.
 *
 * @return ImageBitmap that can be used directly in Compose, or null if loading fails.
 */
expect fun ImageAsset.loadImageBitmap(): ImageBitmap?

/**
 * Extension function to load the photo as Painter for Compose usage.
 * Perfect for direct use with Image composable and painterResource.
 *
 * @return Painter that can be used directly in Image composable, or null if loading fails.
 */
expect fun ImageAsset.loadPainter(): Painter?
