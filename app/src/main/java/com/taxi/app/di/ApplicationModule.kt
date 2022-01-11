package com.taxi.app.di

import android.app.Application
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.taxi.app.data.local.prefs.UserPreferenceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    fun provideLinearLayoutManager(@ApplicationContext context: Context): LinearLayoutManager {
        return LinearLayoutManager(context)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().setPrettyPrinting().setLenient().create()
    }

    @Singleton
    @Provides
    fun provideUserPreferenceProvider(@ApplicationContext context: Context): UserPreferenceProvider {
        return UserPreferenceProvider(context)
    }
}