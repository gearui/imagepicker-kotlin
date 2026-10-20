# imagepicker

[中文文档](README.zh-Hans.md)

`imagepicker` is a Kotlin Multiplatform image picker/camera library for GearUI apps.

## Coordinates

```kotlin
implementation("com.gearui:imagepicker:<version>")
```

## Modules

- `imagepicker`: image picker core library
- `sample`: GearUI sample app (Android + iOS)
- `gearui-kit-local`: local linkage to GearUI kit for sample development

## Quick start (state API only)

```kotlin
import com.gearui.kit.imagepicker.presentation.api.ImagePicker
import com.gearui.kit.imagepicker.presentation.api.ImagePickerSimpleConfig
import com.gearui.kit.imagepicker.presentation.api.ImagePickerSource
import com.gearui.kit.imagepicker.presentation.api.rememberImagePickerState

val pickerState = rememberImagePickerState()

Button(onClick = { pickerState.show() }) { Text("Pick image") }

ImagePicker(
    state = pickerState,
    onResult = { result -> /* handle Single/Multiple */ },
    onError = { e -> /* handle error */ },
    config = ImagePickerSimpleConfig(
        source = ImagePickerSource.CameraOrGallery,
        multiple = false,
        cropEnabled = true
    )
)
```

Only state-driven APIs are supported.

Single-image launcher:

```kotlin
import com.gearui.kit.imagepicker.presentation.api.ImagePickerSource
import com.gearui.kit.imagepicker.presentation.api.rememberSingleImagePickerLauncher

val launchSinglePicker = rememberSingleImagePickerLauncher(
    source = ImagePickerSource.GalleryOnly,
    onPicked = { asset -> /* upload */ },
    onError = { e -> /* handle */ }
)

Button(onClick = launchSinglePicker) { Text("Pick") }
```

## Bytes API

```kotlin
import com.gearui.kit.imagepicker.domain.extensions.readBytes
import com.gearui.kit.imagepicker.domain.models.BytesPolicy

val original = asset.readBytes(BytesPolicy.ORIGINAL)
val optimized = asset.readBytes(BytesPolicy.OPTIMIZED)
```

- `readBytes()` is async and policy-driven.
- `loadBytes()` remains as a compatibility wrapper (sync, internally maps to optimized policy).

## Platform capability matrix

| Platform | Gallery | Camera | Crop |
|---|---|---|---|
| Android | ✅ | ✅ | ✅ |
| iOS (real device) | ✅ | ✅ | ✅ |
| iOS Simulator | ✅ | ⚠️ runtime/simulator dependent | ✅ |
| JVM Desktop | ✅ (file picker) | ❌ | ❌ |
| JS (Browser) | ✅ | ✅ (browser permission/device support) | ✅ |
| WASM | ❌ | ❌ | ❌ |

Unsupported capability returns `UnsupportedPlatformException` with `ImagePickerErrorCode`:

- `PLATFORM_NOT_SUPPORTED`
- `CAMERA_NOT_SUPPORTED`
- `GALLERY_NOT_SUPPORTED`
- `CROP_NOT_SUPPORTED`

## Development

```bash
./gradlew :imagepicker:build
./gradlew :sample:assembleDebug
```

## License

MIT (follow upstream project policy in `LICENSE`).
