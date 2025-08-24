package di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration = {}) {
    startKoin {
        config()
        modules(
            networkModule,
            databaseModule,
            dataModule,
            appModule,
            commonDatabaseModule
        )
    }
}