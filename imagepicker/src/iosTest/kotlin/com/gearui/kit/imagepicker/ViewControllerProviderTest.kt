package com.gearui.kit.imagepicker

import com.gearui.kit.imagepicker.domain.utils.ViewControllerProvider
import kotlin.test.Test
import kotlin.test.assertTrue

class ViewControllerProviderTest {
    
    @Test
    fun `ViewControllerProvider class should be accessible`() {
        assertTrue(ViewControllerProvider::class.simpleName == "ViewControllerProvider")
    }
}