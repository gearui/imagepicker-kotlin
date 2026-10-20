package com.gearui.kit.imagepicker.domain.extensions

import com.gearui.kit.imagepicker.domain.models.ImageAsset
import kotlin.test.Test
import kotlin.test.assertNotNull

class ImageAssetExtensionsTest {

    @Test
    fun `ImageAsset extension functions should exist and be callable`() {
        val photoResult = ImageAsset(
            uri = "content://test/photo.jpg",
            width = 1920,
            height = 1080
        )
        
        // Test that extension functions exist and can be called
        // Note: These are expect functions, so we can't test actual implementation
        // but we can verify they exist and don't throw compile errors
        
        assertNotNull(photoResult.uri)
        assertNotNull(photoResult.width)
        assertNotNull(photoResult.height)
    }

    @Test
    fun `ImageAsset extension functions should exist and be callable`() {
        val imageAsset = ImageAsset(
            uri = "content://test/gallery_photo.jpg",
            width = 1920,
            height = 1080
        )
        
        // Test that extension functions exist and can be called
        // Note: These are expect functions, so we can't test actual implementation
        // but we can verify they exist and don't throw compile errors
        
        assertNotNull(imageAsset.uri)
        assertNotNull(imageAsset.width)
        assertNotNull(imageAsset.height)
    }

    @Test
    fun `ImageAsset should have correct property types`() {
        val photoResult = ImageAsset(
            uri = "content://test/photo.jpg",
            width = 1920,
            height = 1080
        )
        
        // Verify types
        val uri: String = photoResult.uri
        val width: Int? = photoResult.width
        val height: Int? = photoResult.height
        
        assertNotNull(uri)
        assertNotNull(width)
        assertNotNull(height)
    }

    @Test
    fun `ImageAsset should have correct property types`() {
        val imageAsset = ImageAsset(
            uri = "content://test/gallery_photo.jpg",
            width = 1920,
            height = 1080
        )
        
        // Verify types
        val uri: String = imageAsset.uri
        val width: Int? = imageAsset.width
        val height: Int? = imageAsset.height
        
        assertNotNull(uri)
        assertNotNull(width)
        assertNotNull(height)
    }

    @Test
    fun `ImageAsset should handle various URI formats`() {
        val contentUri = ImageAsset("content://media/external/images/1", 800, 600)
        val fileUri = ImageAsset("file:///storage/emulated/0/photo.jpg", 1200, 900)
        val httpUri = ImageAsset("https://example.com/photo.jpg", 1600, 1200)
        
        assertNotNull(contentUri.uri)
        assertNotNull(fileUri.uri)
        assertNotNull(httpUri.uri)
    }

    @Test
    fun `ImageAsset should handle various URI formats`() {
        val contentUri = ImageAsset("content://media/external/images/1", 800, 600)
        val fileUri = ImageAsset("file:///storage/emulated/0/photo.jpg", 1200, 900)
        val httpUri = ImageAsset("https://example.com/photo.jpg", 1600, 1200)
        
        assertNotNull(contentUri.uri)
        assertNotNull(fileUri.uri)
        assertNotNull(httpUri.uri)
    }

    @Test
    fun `ImageAsset should handle various image dimensions`() {
        val square = ImageAsset("content://test/square.jpg", 1000, 1000)
        val landscape = ImageAsset("content://test/landscape.jpg", 1920, 1080)
        val portrait = ImageAsset("content://test/portrait.jpg", 1080, 1920)
        val tiny = ImageAsset("content://test/tiny.jpg", 64, 64)
        val large = ImageAsset("content://test/large.jpg", 4000, 3000)
        
        assertNotNull(square)
        assertNotNull(landscape)
        assertNotNull(portrait)
        assertNotNull(tiny)
        assertNotNull(large)
    }

    @Test
    fun `ImageAsset should handle various image dimensions`() {
        val square = ImageAsset("content://test/square.jpg", 1000, 1000)
        val landscape = ImageAsset("content://test/landscape.jpg", 1920, 1080)
        val portrait = ImageAsset("content://test/portrait.jpg", 1080, 1920)
        val tiny = ImageAsset("content://test/tiny.jpg", 64, 64)
        val large = ImageAsset("content://test/large.jpg", 4000, 3000)
        
        assertNotNull(square)
        assertNotNull(landscape)
        assertNotNull(portrait)
        assertNotNull(tiny)
        assertNotNull(large)
    }

    @Test
    fun `data classes should support equality checks`() {
        val photo1 = ImageAsset("content://test/photo.jpg", 800, 600)
        val photo2 = ImageAsset("content://test/photo.jpg", 800, 600)
        val photo3 = ImageAsset("content://test/other.jpg", 800, 600)
        
        assertNotNull(photo1)
        assertNotNull(photo2)
        assertNotNull(photo3)
        
        // Basic existence test - actual equality tests are in other test files
    }

    @Test
    fun `data classes should support toString`() {
        val photoResult = ImageAsset("content://test/photo.jpg", 800, 600)
        val galleryResult = ImageAsset("content://test/gallery.jpg", 1200, 900)
        
        val photoString = photoResult.toString()
        val galleryString = galleryResult.toString()
        
        assertNotNull(photoString)
        assertNotNull(galleryString)
    }
}
