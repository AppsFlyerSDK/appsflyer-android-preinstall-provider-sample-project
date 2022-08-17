package com.appsflyer.oem.internal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appsflyer.oem.PreInstallIdEntity

@Dao
internal interface PreInstallDao {
    @Query("SELECT * from PreInstallIdEntity where appId = :appId")
    fun select(appId: String): PreInstallIdEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(referrer: PreInstallIdEntity)
}