package com.taxi.app.di

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun provideFragmentManager(fragmentActivity: FragmentActivity): FragmentManager {
        return fragmentActivity.supportFragmentManager
    }

}