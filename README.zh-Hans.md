# imagepicker

`imagepicker` 是一个为 GearUI + KuiklyUI 项目构建的图片选择器/相机库。

## 依赖引入

```kotlin
implementation("com.gearui:imagepicker:<version>")
```

## 模块

- `imagepicker`：图片选择器核心库
- `sample`：GearUI 示例应用（Android + iOS）
- `gearui-kit-local`：用于示例开发的 GearUI kit 本地链接

## 平台支持状态

### iOS

- `从相册选择`：已支持
- `拍照`：真机已支持
- iOS 模拟器的相机回调在不同运行时版本中不稳定，请使用真机验证相机流程

### Android

- `从相册选择`：已支持，通过 `ActivityResultContracts.GetContent` 实现
- `拍照`：已支持，通过 `ActivityResultContracts.TakePicture` + `FileProvider` 实现
- 运行时相机权限由 Bridge 处理（`Manifest.permission.CAMERA`）

## 运行示例

### Android

```bash
./gradlew :sample:assembleDebug
./gradlew :sample:installDebug
```

### iOS

```bash
cd sample/iosApp
xcodebuild -workspace GearUISample.xcworkspace -scheme iosApp -configuration Debug
```

或在 Xcode 中打开 `sample/iosApp/GearUISample.xcworkspace`，运行 `iosApp` scheme。

## Android 集成说明

`sample` 展示了原生 Bridge 模式：

- 在 `MainActivity.onCreate` 中调用 `NativeImagePickerBridge.bind(activity)`
- 在 `MainActivity.onDestroy` 中调用 `NativeImagePickerBridge.unbind(activity)`
- 公共代码中的 Bridge API：

```kotlin
NativeImagePickerBridge.open(
    source = ImagePickerSource.CAMERA, // 或 GALLERY
    onPicked = { uri -> /* 处理图片 */ },
    onError = { message -> /* 显示错误 */ },
    onCancel = { /* 处理取消 */ }
)
```

Manifest 配置要求（示例中已配置）：

- `android.permission.CAMERA`
- `androidx.core.content.FileProvider` 配合 `@xml/file_paths`

## iOS 集成说明

`sample` 使用 Kotlin/Native 调用 UIKit 选择器，并在调用 `presentViewController` 之前将展示操作调度到主线程。

Info.plist 配置要求（示例中已配置）：

- `NSCameraUsageDescription`
- `NSPhotoLibraryUsageDescription`
- `NSPhotoLibraryAddUsageDescription`

## 开发

```bash
./gradlew :imagepicker:build
./gradlew :sample:assembleDebug
```

## 许可证

MIT（遵循 `LICENSE` 文件中的上游项目策略）。
