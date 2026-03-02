package com.gearui.kit.imagepicker.domain.utils

import android.media.MediaActionSound

fun playShutterSound() {
    val sound = MediaActionSound()
    sound.play(MediaActionSound.SHUTTER_CLICK)
}
