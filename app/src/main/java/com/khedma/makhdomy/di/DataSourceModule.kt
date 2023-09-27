package com.khedma.makhdomy.di

import com.khedma.makhdomy.data.local.MakhdomLocalDataSource
import com.khedma.makhdomy.data.local.MakhdomLocalDataSourceImpl
import com.khedma.makhdomy.data.repository.MakhdomRepositoryImpl
import com.khedma.makhdomy.domain.repository.MakhdomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun provideMakhdomLocalDataSource(makhdomLocalDataSourceImpl: MakhdomLocalDataSourceImpl): MakhdomLocalDataSource


    @Singleton
    @Binds
    abstract fun provideMakhdomRepository(makhdomRepositoryImpl: MakhdomRepositoryImpl): MakhdomRepository

}