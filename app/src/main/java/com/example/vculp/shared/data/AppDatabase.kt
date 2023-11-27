package com.example.vculp.shared.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vculp.features.chooseLocation.data.FavLocationsDao
import com.example.vculp.features.chooseLocation.data.models.FavLocation
import com.example.vculp.shared.data.converter.LinkTypeConverter
import com.example.vculp.shared.data.models.FavRegionDataItem

@Database(entities = [FavRegionDataItem::class], version = 3)
@TypeConverters(LinkTypeConverter::class)
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