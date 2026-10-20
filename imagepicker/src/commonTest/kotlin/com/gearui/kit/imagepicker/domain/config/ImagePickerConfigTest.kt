package com.gearui.kit.imagepicker.domain.config

import com.gearui.kit.imagepicker.domain.models.CapturePhotoPreference
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import com.gearui.kit.imagepicker.domain.models.ImagePickerResult
import com.gearui.kit.imagepicker.domain.models.MimeType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ImagePickerConfigTest {

    @Test
    fun `GalleryConfig should have default values`() {
        val config = GalleryConfig()
        
        assertEquals(30, config.selectionLimit)
        assertEquals(listOf(MimeType.IMAGE_ALL), config.mimeTypes)
        assertFalse(config.allowMultiple)
    }

    @Test
    fun `GalleryConfig should allow custom values`() {
        val customMimeTypes = listOf(MimeType.IMAGE_JPEG, MimeType.IMAGE_PNG)
        val config = GalleryConfig(
            selectionLimit = 5,
            mimeTypes = customMimeTypes,
            allowMultiple = true
        )
        
        assertEquals(5, config.selectionLimit)
        assertEquals(customMimeTypes, config.mimeTypes)
        assertTrue(config.allowMultiple)
    }

    @Test
    fun `CameraCaptureConfig should have default values`() {
        val config = CameraCaptureConfig()
        
        assertEquals(CapturePhotoPreference.BALANCED, config.preference)
    }

    @Test
    fun `CameraCaptureConfig should allow custom values`() {
        val config = CameraCaptureConfig(
            preference = CapturePhotoPreference.FAST
        )
        
        assertEquals(CapturePhotoPreference.FAST, config.preference)
    }

    @Test
    fun `UiConfig should have default values`() {
        val config = UiConfig()
        
        // All fields are nullable with default null values
        assertEquals(null, config.buttonColor)
        assertEquals(null, config.iconColor)
        assertEquals(null, config.buttonSize)
        assertEquals(null, config.flashIcon)
        assertEquals(null, config.switchCameraIcon)
        assertEquals(null, config.galleryIcon)
    }

    @Test
    fun `CameraCallbacks should have default values`() {
        val callbacks = CameraCallbacks()
        
        assertEquals(null, callbacks.onCameraReady)
        assertEquals(null, callbacks.onCameraSwitch)
        assertEquals(null, callbacks.onPermissionError)
        assertEquals(null, callbacks.onGalleryOpened)
    }

    @Test
    fun `PermissionAndConfirmationConfig should have default values`() {
        val config = PermissionAndConfirmationConfig()
        
        assertEquals(null, config.customPermissionHandler)
        assertEquals(null, config.customConfirmationView)
        assertEquals(null, config.customDeniedDialog)
        assertEquals(null, config.customSettingsDialog)
    }

    @Test
    fun `CameraPreviewConfig should have default values`() {
        val config = CameraPreviewConfig()
        
        assertFalse(config.captureButtonSize.value == 0f)
        assertEquals(null, config.uiConfig.buttonColor)
        assertEquals(null, config.cameraCallbacks.onCameraReady)
    }

    @Test
    fun `ImagePickerConfig should work with lambda parameters`() {
        var resultTriggered = false
        var errorOccurred = false
        var dismissed = false

        val config = ImagePickerConfig(
            onResult = { resultTriggered = result is ImagePickerResult.Single },
            onError = { errorOccurred = true },
            onDismiss = { dismissed = true }
        )

        // Test that lambdas are stored
        config.onResult(ImagePickerResult.Single(ImageAsset("test", 100, 100)))
        config.onError(Exception("test"))
        config.onDismiss()

        assertTrue(resultTriggered)
        assertTrue(errorOccurred)
        assertTrue(dismissed)
    }

    @Test
    fun `ImagePickerConfig should have correct default strings`() {
        val config = ImagePickerConfig(
            onResult = {},
            onError = {}
        )

        assertEquals("Select option", config.dialogTitle)
        assertEquals("Take photo", config.takePhotoText)
        assertEquals("Select from gallery", config.selectFromGalleryText)
        assertEquals("Cancel", config.cancelText)
        assertFalse(config.directCameraLaunch)
        assertFalse(config.directGalleryLaunch)
    }
}
