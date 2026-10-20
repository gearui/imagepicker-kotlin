# imagepicker

`imagepicker` 是用于 GearUI 应用的 Kotlin Multiplatform 图片选择器/相机库。

## 依赖引入

```kotlin
implementation("com.gearui:imagepicker:<version>")
```

## 模块

- `imagepicker`：图片选择器核心库
- `sample`：GearUI 示例应用（Android + iOS）
- `gearui-kit-local`：用于示例开发的 GearUI kit 本地链接

## 快速开始（仅推荐 state API）

```kotlin
import com.gearui.kit.imagepicker.presentation.api.ImagePicker
import com.gearui.kit.imagepicker.presentation.api.ImagePickerSimpleConfig
import com.gearui.kit.imagepicker.presentation.api.ImagePickerSource
import com.gearui.kit.imagepicker.presentation.api.rememberImagePickerState

val pickerState = rememberImagePickerState()

Button(onClick = { pickerState.show() }) { Text("选择图片") }

ImagePicker(
    state = pickerState,
    onResult = { result -> /* 处理 Single/Multiple */ },
    onError = { e -> /* 处理异常 */ },
    config = ImagePickerSimpleConfig(
        source = ImagePickerSource.CameraOrGallery,
        multiple = false,
        cropEnabled = true
    )
)
```

仅支持 state 驱动 API。

单图 launcher：

```kotlin
import com.gearui.kit.imagepicker.presentation.api.ImagePickerSource
import com.gearui.kit.imagepicker.presentation.api.rememberSingleImagePickerLauncher

val launchSinglePicker = rememberSingleImagePickerLauncher(
    source = ImagePickerSource.GalleryOnly,
    onPicked = { asset -> /* 上传 */ },
    onError = { e -> /* 处理异常 */ }
)

Button(onClick = launchSinglePicker) { Text("选择图片") }
```

## 图片字节读取 API

```kotlin
import com.gearui.kit.imagepicker.domain.extensions.readBytes
import com.gearui.kit.imagepicker.domain.models.BytesPolicy

val original = asset.readBytes(BytesPolicy.ORIGINAL)
val optimized = asset.readBytes(BytesPolicy.OPTIMIZED)
```

- `readBytes()`：异步 + 策略化读取。
- `loadBytes()`：兼容包装（同步，内部走 optimized 策略）。

## 平台能力矩阵

| 平台 | 相册 | 相机 | 裁剪 |
|---|---|---|---|
| Android | ✅ | ✅ | ✅ |
| iOS（真机） | ✅ | ✅ | ✅ |
| iOS 模拟器 | ✅ | ⚠️ 依赖运行时/模拟器 | ✅ |
| JVM Desktop | ✅（文件选择） | ❌ | ❌ |
| JS（浏览器） | ✅ | ✅（依赖浏览器权限和设备） | ✅ |
| WASM | ❌ | ❌ | ❌ |

不支持能力时会返回 `UnsupportedPlatformException`，并带 `ImagePickerErrorCode`：

- `PLATFORM_NOT_SUPPORTED`
- `CAMERA_NOT_SUPPORTED`
- `GALLERY_NOT_SUPPORTED`
- `CROP_NOT_SUPPORTED`

## 开发

```bash
./gradlew :imagepicker:build
./gradlew :sample:assembleDebug
```

## 许可证

MIT（遵循 `LICENSE` 文件中的上游项目策略）。
