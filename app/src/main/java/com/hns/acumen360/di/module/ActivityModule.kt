package com.hns.acumen360.di.module


import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hns.acumen360.base.BaseActivity
import com.hns.acumen360.base.ViewModelProviderFactory
import com.hns.acumen360.data.respository.CommonRepository
import com.hns.acumen360.data.respository.RemoteGalaxyRepository
import com.hns.acumen360.data.respository.RemoteStarRepository
import com.hns.acumen360.data.viewmodel.CommonViewModel
import com.hns.acumen360.data.viewmodel.RemoteGalaxyViewModel
import com.hns.acumen360.data.viewmodel.RemoteStarViewModel
import com.hns.acumen360.utils.network.NetworkHelper
import com.hns.acumen360.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class ActivityModule(private val activity: BaseActivity<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity)


    @Provides
    fun provideCommonViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        repository: CommonRepository
    ): CommonViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(CommonViewModel::class) {
            CommonViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                repository
            )
        })[CommonViewModel::class.java]

    @Provides
    fun provideRemoteStarViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        repository: CommonRepository,
        remoteStarRepository: RemoteStarRepository,
        commonViewModel: CommonViewModel
    ): RemoteStarViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(RemoteStarViewModel::class) {
            RemoteStarViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                repository,
                remoteStarRepository,
                commonViewModel)
        })[RemoteStarViewModel::class.java]

    @Provides
    fun provideRemoteGalaxyViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        repository: CommonRepository,
        remoteGalaxyRepository: RemoteGalaxyRepository,
        commonViewModel: CommonViewModel
    ): RemoteGalaxyViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(RemoteGalaxyViewModel::class) {
            RemoteGalaxyViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                repository,
                remoteGalaxyRepository,
                commonViewModel)
        })[RemoteGalaxyViewModel::class.java]


}