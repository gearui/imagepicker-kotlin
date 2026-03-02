# imagepicker

[中文文档](README.zh-Hans.md)

`imagepicker` is an image picker/camera library built for GearUI + KuiklyUI projects.

## Coordinates

```kotlin
implementation("com.gearui:imagepicker:<version>")
```

## Modules

- `imagepicker`: image picker core library
- `sample`: GearUI sample app (Android + iOS)
- `gearui-kit-local`: local linkage to GearUI kit for sample development

## Current platform status

### iOS

- `Choose from gallery`: supported
- `Take photo`: supported on real devices
- iOS Simulator camera callback is not reliable across runtime versions; use real device to validate camera flow

### Android

- `Choose from gallery`: supported via `ActivityResultContracts.GetContent`
- `Take photo`: supported via `ActivityResultContracts.TakePicture` + `FileProvider`
- Runtime camera permission handled by bridge (`Manifest.permission.CAMERA`)

## Sample run

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

Or open `sample/iosApp/GearUISample.xcworkspace` in Xcode and run `iosApp` scheme.

## Android integration notes

`sample` demonstrates the native bridge pattern:

- `NativeImagePickerBridge.bind(activity)` in `MainActivity.onCreate`
- `NativeImagePickerBridge.unbind(activity)` in `MainActivity.onDestroy`
- Bridge API from common code:

```kotlin
NativeImagePickerBridge.open(
    source = ImagePickerSource.CAMERA, // or GALLERY
    onPicked = { uri -> /* handle image */ },
    onError = { message -> /* show error */ },
    onCancel = { /* handle cancel */ }
)
```

Manifest requirements (already configured in sample):

- `android.permission.CAMERA`
- `androidx.core.content.FileProvider` with `@xml/file_paths`

## iOS integration notes

`sample` uses UIKit picker from Kotlin/Native and dispatches presentation to main thread before calling `presentViewController`.

Info.plist requirements (already configured in sample):

- `NSCameraUsageDescription`
- `NSPhotoLibraryUsageDescription`
- `NSPhotoLibraryAddUsageDescription`

## Development

```bash
./gradlew :imagepicker:build
./gradlew :sample:assembleDebug
```

## License

MIT (follow upstream project policy in `LICENSE`).
