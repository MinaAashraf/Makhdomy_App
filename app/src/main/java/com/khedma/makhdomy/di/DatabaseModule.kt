package com.khedma.makhdomy.di

import android.content.Context
import androidx.room.Room
import com.khedma.makhdomy.R
import com.khedma.makhdomy.data.local.MakhdomLocalDataSource
import com.khedma.makhdomy.data.local.MakhdomLocalDataSourceImpl
import com.khedma.makhdomy.data.local.MakhdomyDao
import com.khedma.makhdomy.data.local.MakhdomyDb
import com.khedma.makhdomy.data.repository.MakhdomRepositoryImpl
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import com.khedma.makhdomy.domain.utils.Makhdom_Local_Database_Name
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideMakhdomDatabase(@ApplicationContext context: Context): MakhdomyDb =
        Room.databaseBuilder(
            context,
            MakhdomyDb::class.java,
            Makhdom_Local_Database_Name
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideMakhdomDao(makhdomyDb: MakhdomyDb): MakhdomyDao = makhdomyDb.getDao()



}