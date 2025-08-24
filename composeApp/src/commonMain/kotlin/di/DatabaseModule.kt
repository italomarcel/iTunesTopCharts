package di

import app.cash.sqldelight.db.SqlDriver
import com.company.itunes.database.iTunesAlbumsDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

val commonDatabaseModule = module {
    single<iTunesAlbumsDatabase> {
        iTunesAlbumsDatabase(get<SqlDriver>())
    }
}

expect val databaseModule: Module