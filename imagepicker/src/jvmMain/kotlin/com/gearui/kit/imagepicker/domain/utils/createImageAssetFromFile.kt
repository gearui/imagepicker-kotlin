package com.gearui.kit.imagepicker.domain.utils

import com.gearui.kit.imagepicker.domain.models.ImageAsset
import java.io.File
import javax.imageio.ImageIO

fun createImageAssetFromFile(file: File): ImageAsset? {
    return try {
        if (!file.exists() || !file.isFile) return null

        val bufferedImage = ImageIO.read(file)
        if (bufferedImage == null) return null

        val width = bufferedImage.width
        val height = bufferedImage.height
        val fileName = file.name
        val fileSize = (file.length() / 1024)
        val uri = file.toURI().toString()

        ImageAsset(
            uri = uri,
            width = width,
            height = height,
            fileName = fileName,
            fileSize = fileSize
        )
    } catch (_: Exception) {
        null
    }
}
