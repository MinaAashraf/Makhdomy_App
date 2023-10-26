package com.khedma.makhdomy.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.khedma.makhdomy.data.local.MakhdomyDao
import com.khedma.makhdomy.data.local.MakhdomyDb
import com.khedma.makhdomy.domain.utils.Makhdom_Local_Database_Name
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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


    @Singleton
    @Provides
    fun provideFirebaseFirestore () : FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseStorage () : FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAuth () : FirebaseAuth = FirebaseAuth.getInstance()





}