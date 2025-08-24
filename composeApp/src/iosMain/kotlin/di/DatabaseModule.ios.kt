package di

import com.company.itunes.database.iTunesAlbumsDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual val databaseModule: Module = module {
    single<SqlDriver> {
        NativeSqliteDriver(
            iTunesAlbumsDatabase.Schema,
            "itunes_albums.db"
        )
    }
}