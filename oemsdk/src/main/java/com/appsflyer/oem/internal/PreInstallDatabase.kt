package com.appsflyer.oem.internal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appsflyer.oem.PreInstallId

@Database(entities = [PreInstallId::class], version = 4)
internal abstract class PreInstallDatabase : RoomDatabase() {
    companion object {
        private var instance: PreInstallDatabase? = null
        fun get(context: Context): PreInstallDatabase {
            if (instance == null) instance =
                Room.databaseBuilder(context, PreInstallDatabase::class.java, "referrer_database")
                    .build()
            return instance as PreInstallDatabase
        }
    }

    abstract fun referrerDao(): PreInstallDao
}