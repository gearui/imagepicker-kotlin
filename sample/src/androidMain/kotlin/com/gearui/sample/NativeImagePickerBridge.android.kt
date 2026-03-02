package com.gearui.sample

import android.Manifest
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual object NativeImagePickerBridge {
    private var hostActivity: ComponentActivity? = null
    private var callbacks: PickerCallbacks? = null
    private var pendingCameraUri: Uri? = null

    private var getContentLauncher: ActivityResultLauncher<String>? = null
    private var takePictureLauncher: ActivityResultLauncher<Uri>? = null
    private var cameraPermissionLauncher: ActivityResultLauncher<String>? = null

    fun bind(activity: ComponentActivity) {
        hostActivity = activity

        getContentLauncher = activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val current = callbacks
            callbacks = null
            if (uri != null) {
                current?.onPicked?.invoke(uri.toString())
            } else {
                current?.onCancel?.invoke()
            }
        }

        takePictureLauncher = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val current = callbacks
            val uri = pendingCameraUri
            callbacks = null
            pendingCameraUri = null
            if (success && uri != null) {
                current?.onPicked?.invoke(uri.toString())
            } else {
                current?.onCancel?.invoke()
            }
        }

        cameraPermissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    launchCameraInternal()
                } else {
                    callbacks?.onError?.invoke("Camera permission denied")
                    callbacks = null
                }
            }
    }

    fun unbind(activity: ComponentActivity) {
        if (hostActivity == activity) {
            hostActivity = null
            callbacks = null
            pendingCameraUri = null
            getContentLauncher = null
            takePictureLauncher = null
            cameraPermissionLauncher = null
        }
    }

    actual fun open(
        source: ImagePickerSource,
        onPicked: (uri: String) -> Unit,
        onError: (message: String) -> Unit,
        onCancel: () -> Unit
    ) {
        val activity = hostActivity
        if (activity == null) {
            onError("ImagePicker bridge is not bound to Activity")
            return
        }

        callbacks = PickerCallbacks(
            onPicked = onPicked,
            onError = onError,
            onCancel = onCancel
        )

        when (source) {
            ImagePickerSource.GALLERY -> {
                val launcher = getContentLauncher
                if (launcher == null) {
                    callbacks = null
                    onError("Gallery launcher unavailable")
                    return
                }
                launcher.launch("image/*")
            }

            ImagePickerSource.CAMERA -> {
                val launcher = cameraPermissionLauncher
                if (launcher == null) {
                    callbacks = null
                    onError("Camera permission launcher unavailable")
                    return
                }
                launcher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun launchCameraInternal() {
        val activity = hostActivity ?: return
        val uri = createTempImageUri(activity)
        if (uri == null) {
            callbacks?.onError?.invoke("Failed to create output file for camera")
            callbacks = null
            return
        }

        pendingCameraUri = uri
        val launcher = takePictureLauncher
        if (launcher == null) {
            callbacks?.onError?.invoke("Camera launcher unavailable")
            callbacks = null
            pendingCameraUri = null
            return
        }
        launcher.launch(uri)
    }

    private fun createTempImageUri(activity: ComponentActivity): Uri? {
        return try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val file = File(activity.cacheDir, "gearui_camera_$timestamp.jpg")
            FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.fileprovider",
                file
            )
        } catch (_: Throwable) {
            null
        }
    }

    private data class PickerCallbacks(
        val onPicked: (String) -> Unit,
        val onError: (String) -> Unit,
        val onCancel: () -> Unit
    )
}

