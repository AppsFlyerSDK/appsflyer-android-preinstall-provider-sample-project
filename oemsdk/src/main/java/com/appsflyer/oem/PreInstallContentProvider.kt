package com.appsflyer.oem

import android.content.ContentProvider
import android.content.ContentValues
import android.database.MatrixCursor
import android.net.Uri
import android.os.Binder
import android.os.Build

class PreInstallContentProvider : ContentProvider() {
    private lateinit var dao: PreInstallDao
    override fun onCreate(): Boolean {
        dao = PreInstallDatabase.get(context!!).referrerDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ) = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) callingPackage
    else context!!.packageManager.getNameForUid(Binder.getCallingUid()))!!
        .let { dao.select(it) }
        ?.let { arrayOf(it.preloadId) }
        ?.let { array ->
            arrayOf(PreInstallEntity.KEY_PRELOAD_ID)
                .let { MatrixCursor(it) }
                .also { it.addRow(array) }
        }

    override fun getType(uri: Uri): Nothing? = null
    override fun insert(uri: Uri, values: ContentValues?): Nothing? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ) = 0
}