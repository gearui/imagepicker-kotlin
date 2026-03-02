package com.gearui.kit.imagepicker.presentation.ui.utils

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleOwner
import com.gearui.kit.imagepicker.data.camera.CameraController
import com.gearui.kit.imagepicker.data.camera.CameraXManager
import com.gearui.kit.imagepicker.data.managers.FileManager
import com.gearui.kit.imagepicker.data.processors.ImageOrientationCorrector
import com.gearui.kit.imagepicker.data.processors.ImageProcessor
import com.gearui.kit.imagepicker.domain.utils.DefaultLogger
import com.gearui.kit.imagepicker.presentation.viewModel.ImagePickerViewModel


@Composable
fun rememberImagePickerViewModel(): ImagePickerViewModel {
    return remember { 
        ImagePickerViewModel(
            logger = DefaultLogger
        )
    }
}


@Composable
fun rememberCameraManager(
    context: Context,
    lifecycleOwner: LifecycleOwner
): CameraXManager? {
    return remember(lifecycleOwner) {
        try {
            val fileManager = FileManager(context)
            val orientationCorrector = ImageOrientationCorrector()
            val imageProcessor = ImageProcessor(context,fileManager, orientationCorrector)
            val cameraController = CameraController(context, lifecycleOwner, fileManager)
            
            CameraXManager(cameraController, imageProcessor)
        } catch (e: Exception) {
            Log.e("ImagePicker", "Failed to create camera manager", e)
            null
        }
    }
}
