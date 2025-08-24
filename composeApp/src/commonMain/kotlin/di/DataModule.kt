package di

import data.local.AlbumsLocalDataSource
import data.local.SQLDelightLocalDataSource
import data.remote.AlbumsRemoteDataSource
import data.remote.ITunesApi
import data.repository.AlbumsRepositoryImpl
import domain.repository.AlbumsRepository
import domain.usecase.GetTopAlbumsUseCase
import domain.usecase.RefreshAlbumsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    includes(databaseModule, networkModule)

    singleOf(::SQLDelightLocalDataSource) bind AlbumsLocalDataSource::class
    singleOf(::ITunesApi) bind AlbumsRemoteDataSource::class

    singleOf(::AlbumsRepositoryImpl) bind AlbumsRepository::class

    singleOf(::GetTopAlbumsUseCase)
    singleOf(::RefreshAlbumsUseCase)
}