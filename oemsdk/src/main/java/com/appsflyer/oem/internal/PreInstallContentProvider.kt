package com.appsflyer.oem.internal

import android.content.ContentProvider
import android.content.ContentValues
import android.database.MatrixCursor
import android.net.Uri
import com.appsflyer.oem.PreInstallId

internal class PreInstallContentProvider : ContentProvider() {
    companion object {
        // For testing purposes
        var delayMillis = 0L
    }
    private lateinit var dao: PreInstallDao
    override fun onCreate(): Boolean {
        dao = PreInstallDatabase.get(context!!).preInstallDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ) = callingPackage!!
        .let(dao::select)
        ?.transactionId
        ?.let { arrayOf(it) }
        ?.let { preloadIds ->
            // For testing purposes
            Thread.sleep(delayMillis)
            MatrixCursor(arrayOf(PreInstallId.KEY_TRANSACTION_ID))
                .apply {
                    addRow(preloadIds)
                }
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