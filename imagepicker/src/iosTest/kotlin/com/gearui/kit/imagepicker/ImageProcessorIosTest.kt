package com.gearui.kit.imagepicker

import com.gearui.kit.imagepicker.data.processors.ImageProcessor
import kotlin.test.Test
import kotlin.test.assertTrue

class ImageProcessorIosTest {
    
    @Test
    fun `ImageProcessor class should be accessible`() {
        assertTrue(ImageProcessor::class.simpleName == "ImageProcessor")
    }
}