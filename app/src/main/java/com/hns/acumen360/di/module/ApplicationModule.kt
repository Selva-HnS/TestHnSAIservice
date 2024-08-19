package com.hns.acumen360.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.hns.acumen360.base.BaseApplication
import com.hns.acumen360.data.local.db.AppDatabase
import com.hns.acumen360.data.remote.OrbitClient
import com.hns.acumen360.data.remote.stars.OrbitStarService
import com.hns.acumen360.data.remote.EndPoints
import com.hns.acumen360.data.remote.galaxy.OrbitGalaxyService
import com.hns.acumen360.utils.network.NetworkHelper
import com.hns.acumen360.utils.rx.RxSchedulerProvider
import com.hns.acumen360.utils.rx.SchedulerProvider
import com.hns.minda.di.ApplicationContext
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: BaseApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = RxSchedulerProvider()

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences("minda-prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDatabaseService(): AppDatabase =
        Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "acumen360-db"
        ).build()

    @Provides
    @Singleton
    fun provideOrbitStarService(): OrbitStarService =
        OrbitClient.createStars(
            EndPoints.BASE_STARS_URL,
            application.cacheDir,
            10 * 1024 * 1024 // 10MB
        )

    @Provides
    @Singleton
    fun provideOrbitGalaxyService(): OrbitGalaxyService =
        OrbitClient.createGalaxy(
            EndPoints.BASE_STARS_URL,
            application.cacheDir,
            10 * 1024 * 1024 // 10MB
        )

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper =
        NetworkHelper(application)

}