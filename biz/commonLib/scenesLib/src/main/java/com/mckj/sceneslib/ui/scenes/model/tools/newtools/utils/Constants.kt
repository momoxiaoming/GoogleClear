package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.Manifest


object Constants {
    const val REQUEST_CODE_PERMISSIONS = 101
    const val REQUEST_CODE_CAMERA = 102
    const val REQUEST_CODE_CROP = 103
    var state = false

    const val DATE_FORMAT = "yyyy-MM-dd HH.mm.ss"
    const val PHOTO_EXTENSION = ".jpg"

    val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )
}