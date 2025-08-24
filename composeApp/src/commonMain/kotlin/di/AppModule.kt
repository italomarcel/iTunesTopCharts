package di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import presentation.viewmodel.AlbumsViewModel

val appModule = module {
    viewModel { AlbumsViewModel(get()) }
}