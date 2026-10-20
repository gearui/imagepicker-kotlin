package com.gearui.kit.imagepicker.domain.models

sealed interface ImagePickerResult {
    data class Single(val asset: ImageAsset) : ImagePickerResult
    data class Multiple(val assets: List<ImageAsset>) : ImagePickerResult
}
