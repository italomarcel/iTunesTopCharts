package di

import org.koin.dsl.module
import presentation.viewmodel.AlbumsViewModel

val appModule = module {
    single { AlbumsViewModel(get(), get()) }
}