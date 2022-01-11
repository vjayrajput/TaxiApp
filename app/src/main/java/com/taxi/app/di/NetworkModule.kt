package com.taxi.app.di

import android.content.Context
import com.google.gson.Gson
import com.taxi.app.BuildConfig
import com.taxi.app.data.local.dao.ItemDao
import com.taxi.app.data.local.prefs.UserPreferenceProvider
import com.taxi.app.data.remote.ApiHeaderInterceptor
import com.taxi.app.data.remote.ApiSource
import com.taxi.app.data.repository.DatabaseRepository
import com.taxi.app.data.repository.UserRepository
import com.taxi.app.utils.CONNECTION_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiSource(retrofit: Retrofit): ApiSource {
        return retrofit.create(ApiSource::class.java)
    }

    @Singleton
    @Provides
    fun provideHttpInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            /**
             * If you uncomment below line then application will crashing due to out of memory error
             */
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return interceptor
    }

    @Singleton
    @Provides
    fun provideApiHeaderInterceptor(
        @ApplicationContext appContext: Context,
        userPreferenceProvider: UserPreferenceProvider
    ): ApiHeaderInterceptor {
        return ApiHeaderInterceptor(appContext, userPreferenceProvider)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        apiHeaderInterceptor: ApiHeaderInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MINUTES)
        okHttpClient.writeTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MINUTES)
        okHttpClient.readTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MINUTES)
        okHttpClient.addInterceptor(apiHeaderInterceptor)
        okHttpClient.addInterceptor(httpLoggingInterceptor)
        okHttpClient.retryOnConnectionFailure(true)
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideUserRepository(apiSource: ApiSource): UserRepository {
        return UserRepository(apiSource)
    }

    @Singleton
    @Provides
    fun provideDatabaseRepository(itemDao: ItemDao): DatabaseRepository {
        return DatabaseRepository(itemDao)
    }

}