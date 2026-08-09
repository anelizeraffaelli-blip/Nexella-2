package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.dao.NexellaDao
import com.example.data.local.entity.BusinessEntity
import com.example.data.local.entity.ConnectionEntity
import com.example.data.local.entity.EllaMessageEntity
import com.example.data.local.entity.MeetingEntity
import com.example.data.local.entity.MeetingParticipantEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.ProfileEntity
import com.example.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProfileEntity::class,
        BusinessEntity::class,
        OpportunityEntity::class,
        ConnectionEntity::class,
        MeetingEntity::class,
        MeetingParticipantEntity::class,
        EllaMessageEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nexellaDao(): NexellaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nexella_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun getInstance(context: Context): AppDatabase = getDatabase(context)
    }
}
