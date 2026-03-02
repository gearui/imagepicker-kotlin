package com.gearui.sample

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSUUID
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.writeToURL
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
actual object NativeImagePickerBridge {
    private var delegateRef: PickerDelegate? = null

    actual fun open(
        source: ImagePickerSource,
        onPicked: (uri: String) -> Unit,
        onError: (message: String) -> Unit,
        onCancel: () -> Unit
    ) {
        dispatch_async(dispatch_get_main_queue()) {
            val root = topViewController()
            if (root == null) {
                onError("Unable to find active view controller")
                return@dispatch_async
            }

            val picker = UIImagePickerController().apply {
                allowsEditing = false
            }

            when (source) {
                ImagePickerSource.CAMERA -> {
                    val available = UIImagePickerController.isSourceTypeAvailable(
                        UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                    )
                    if (!available) {
                        onError("Camera is not available on this device/simulator")
                        return@dispatch_async
                    }
                    picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                }

                ImagePickerSource.GALLERY -> {
                    val available = UIImagePickerController.isSourceTypeAvailable(
                        UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
                    )
                    if (!available) {
                        onError("Photo library is not available")
                        return@dispatch_async
                    }
                    picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
                }
            }

            val delegate = PickerDelegate(
                onPicked = onPicked,
                onError = onError,
                onCancel = onCancel,
                onFinish = { delegateRef = null }
            )
            delegateRef = delegate
            picker.delegate = delegate
            root.presentViewController(picker, animated = true, completion = null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class PickerDelegate(
    private val onPicked: (uri: String) -> Unit,
    private val onError: (message: String) -> Unit,
    private val onCancel: () -> Unit,
    private val onFinish: () -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
        onCancel()
        onFinish()
    }

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        if (image == null) {
            picker.dismissViewControllerAnimated(true, completion = null)
            onError("Failed to read selected image")
            onFinish()
            return
        }

        val imageData = UIImageJPEGRepresentation(image, 0.92)
        if (imageData == null) {
            picker.dismissViewControllerAnimated(true, completion = null)
            onError("Failed to encode image")
            onFinish()
            return
        }

        val fileName = "gearui_picker_${NSUUID().UUIDString}.jpg"
        val fileURL = NSURL.fileURLWithPath("${NSTemporaryDirectory()}$fileName")
        val saved = imageData.writeToURL(fileURL, true)

        picker.dismissViewControllerAnimated(true, completion = null)
        if (saved) {
            onPicked(fileURL?.absoluteString ?: "")
        } else {
            onError("Failed to save image")
        }
        onFinish()
    }
}

private fun topViewController(): UIViewController? {
    var current = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return null
    while (current.presentedViewController != null) {
        current = current.presentedViewController!!
    }
    return current
}
