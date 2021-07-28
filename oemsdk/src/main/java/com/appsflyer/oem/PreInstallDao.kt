package com.appsflyer.oem

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PreInstallDao {
    @Query("SELECT * from PreInstallId where appId = :appId")
    fun select(appId: String): PreInstallId?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(referrer: PreInstallId)
}