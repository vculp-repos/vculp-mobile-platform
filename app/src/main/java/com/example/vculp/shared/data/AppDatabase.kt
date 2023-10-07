package com.example.vculp.shared.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vculp.features.chooseLocation.data.FavLocationsDao
import com.example.vculp.features.chooseLocation.data.models.FavLocation
import java.lang.NullPointerException

@Database(entities = [FavLocation::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract val favLocationsDao: FavLocationsDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null
            fun getInstance(context: Context): AppDatabase{
                synchronized(this){
                    var instance = INSTANCE
                    if(instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "app_database"
                        ).build()
                        INSTANCE = instance
                    }
                    return instance
                }
            }
    }
}