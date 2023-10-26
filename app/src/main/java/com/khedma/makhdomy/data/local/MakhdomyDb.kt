package com.khedma.makhdomy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.khedma.makhdomy.domain.model.Makhdom

@Database(
    entities = [Makhdom::class],
    version = 6,
    exportSchema = false,
)
@TypeConverters (Converters::class)
abstract class MakhdomyDb : RoomDatabase() {
    abstract fun getDao () : MakhdomyDao
}