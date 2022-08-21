package com.appsflyer.oem.internal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appsflyer.oem.models.PreInstallId

@Database(entities = [PreInstallId::class], version = 5)
internal abstract class PreInstallDatabase : RoomDatabase() {
    companion object {
        private var instance: PreInstallDatabase? = null
        fun get(context: Context): PreInstallDatabase {
            if (instance == null) instance =
                Room.databaseBuilder(context, PreInstallDatabase::class.java, "preinstall_database")
                        // in this sample code we don't care about data integrity,
                    .fallbackToDestructiveMigration()
                    .build()
            return instance as PreInstallDatabase
        }
    }

    abstract fun preInstallDao(): PreInstallDao
}