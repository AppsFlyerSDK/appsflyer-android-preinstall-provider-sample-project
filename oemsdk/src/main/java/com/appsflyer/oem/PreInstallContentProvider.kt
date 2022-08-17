package com.appsflyer.oem

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.appsflyer.oem.internal.PreInstallDao
import com.appsflyer.oem.internal.PreInstallDatabase

private const val COLUMN_TRANSACTION_ID = "transaction_id"

class PreInstallContentProvider : ContentProvider() {

    /**
     * Local database to store attribution results - this is
     *
     */
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
    ) : Cursor {
        // for security reasons we should return to each app only data which belongs to it, so verify that
        val callingAppId = callingPackage!!

        // search in our internal db for the record of transaction ids
        val transactionId = dao.select(appId = callingAppId)?.transactionId

        val cursor = MatrixCursor(arrayOf(COLUMN_TRANSACTION_ID))
        // if we have saved transation id - we send it to client, if not - cursor should be empty
        if (transactionId != null) {
            cursor.addRow(arrayOf(transactionId))
        }

        return cursor
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