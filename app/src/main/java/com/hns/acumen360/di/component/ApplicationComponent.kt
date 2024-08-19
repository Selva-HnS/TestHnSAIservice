package com.hns.acumen360.di.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.hns.acumen360.base.BaseApplication
import com.hns.acumen360.data.local.db.AppDatabase
import com.hns.acumen360.data.remote.galaxy.OrbitGalaxyService
import com.hns.acumen360.data.remote.stars.OrbitStarService
import com.hns.acumen360.data.respository.CommonRepository
import com.hns.acumen360.data.respository.RemoteGalaxyRepository
import com.hns.acumen360.data.respository.RemoteStarRepository
import com.hns.acumen360.di.module.ApplicationModule
import com.hns.acumen360.utils.network.NetworkHelper
import com.hns.acumen360.utils.rx.SchedulerProvider
import com.hns.minda.di.ApplicationContext
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(app: BaseApplication)
    fun getApplication(): Application

    @ApplicationContext
    fun getContext(): Context
    fun getOrbitStarService(): OrbitStarService
    fun getOrbitGalaxyService(): OrbitGalaxyService
    fun getDatabaseService(): AppDatabase
    fun getSharedPreferences(): SharedPreferences
    fun getNetworkHelper(): NetworkHelper
    fun getSchedulerProvider(): SchedulerProvider
    fun getCompositeDisposable(): CompositeDisposable
    fun getCommonRepository(): CommonRepository
    fun getRemoteRepository(): RemoteStarRepository
    fun getRemoteGalaxyRepository(): RemoteGalaxyRepository
}