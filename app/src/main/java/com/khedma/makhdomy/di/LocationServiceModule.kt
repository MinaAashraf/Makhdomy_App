package com.khedma.makhdomy.di

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationServiceModule {
    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun provideLocationRequest() = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @Singleton
    @Provides
    fun provideLocationSettingsBuilder(request: LocationRequest): LocationSettingsRequest.Builder =
        LocationSettingsRequest.Builder().addLocationRequest(request).setAlwaysShow(true)

    @Singleton
    @Provides
    fun provideLocationSettingsClient(@ApplicationContext context: Context): SettingsClient =
        LocationServices.getSettingsClient(context)

}