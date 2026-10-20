package com.gearui.kit.imagepicker.domain.exceptions

/**
 * Base exception for ImagePicker operations.
 * 
 * SOLID: Single Responsibility - Only handles exception information
 * SOLID: Open/Closed - Can be extended for specific error types
 */
sealed class ImagePickerException(message: String, cause: Throwable? = null) : Exception(message, cause)

enum class ImagePickerErrorCode {
    PLATFORM_NOT_SUPPORTED,
    CAMERA_NOT_SUPPORTED,
    GALLERY_NOT_SUPPORTED,
    CROP_NOT_SUPPORTED
}

/**
 * Exception thrown when an error occurs during photo capture.
 */
class PhotoCaptureException(message: String, cause: Throwable? = null) : ImagePickerException(message, cause)

/**
 * Exception thrown when permission is denied.
 */
class PermissionDeniedException(message: String, cause: Throwable? = null) : ImagePickerException(message, cause)

/**
 * Exception thrown when image processing fails.
 */
class ImageProcessingException(message: String, cause: Throwable? = null) : ImagePickerException(message, cause)

class UnsupportedPlatformException(
    val code: ImagePickerErrorCode,
    message: String,
    cause: Throwable? = null
) : ImagePickerException(message, cause)
