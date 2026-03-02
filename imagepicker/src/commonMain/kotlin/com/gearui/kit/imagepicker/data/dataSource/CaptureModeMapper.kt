
package com.gearui.kit.imagepicker.data.dataSource

import com.gearui.kit.imagepicker.domain.models.CapturePhotoPreference

internal expect fun getCaptureMode(preference: CapturePhotoPreference): Int
