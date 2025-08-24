package di

import com.company.itunes.database.iTunesAlbumsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual val databaseModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            iTunesAlbumsDatabase.Schema,
            androidContext(),
            "itunes_albums.db"
        )
    }
}