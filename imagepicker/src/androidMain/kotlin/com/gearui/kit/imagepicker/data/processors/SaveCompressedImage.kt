package com.gearui.kit.imagepicker.data.processors

import android.graphics.Bitmap
import com.gearui.kit.imagepicker.domain.models.CompressionLevel
import java.io.File
import java.io.FileOutputStream

 fun saveCompressedImage(
    bitmap: Bitmap,
    originalFile: File,
    compressionLevel: CompressionLevel
): File {
    val outputFile = File(originalFile.parentFile, "compressed_${originalFile.name}")
    val quality = (compressionLevel.toQualityValue() * 100).toInt()

    FileOutputStream(outputFile).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    }

    return outputFile
}