package com.appsflyer.oem

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PreInstallDao {
    @Query("SELECT * from PreInstallEntity where appId = :appId")
    fun select(appId: String): PreInstallEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(referrer: PreInstallEntity)
}