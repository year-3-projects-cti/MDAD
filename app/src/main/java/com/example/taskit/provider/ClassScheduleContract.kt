package com.example.taskit.provider

import android.net.Uri

object ClassScheduleContract {
    const val AUTHORITY = "com.example.taskit.provider"
    const val PATH_CLASSES = "classes"

    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_CLASSES")
}
