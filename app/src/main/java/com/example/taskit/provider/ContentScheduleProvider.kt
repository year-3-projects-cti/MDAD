package com.example.taskit.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.example.taskit.database.AppDatabase

class ClassScheduleProvider : ContentProvider() {

    private lateinit var database: AppDatabase

    override fun onCreate(): Boolean {
        context?.let {
            database = AppDatabase.getDatabase(it)
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uri.lastPathSegment) {
            ClassScheduleContract.PATH_CLASSES -> database.classDao().getAllClassesCursor()
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/${ClassScheduleContract.PATH_CLASSES}"
    }
}
