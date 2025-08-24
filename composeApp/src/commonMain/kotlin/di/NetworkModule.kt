package di

import data.remote.ITunesApi
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import io.ktor.client.request.*
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

private const val NETWORK_TIMEOUT_SECONDS = 30L
private val NETWORK_TIMEOUT = NETWORK_TIMEOUT_SECONDS.seconds

val networkModule = module {

    single(named("jsonConfig")) {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = false
            encodeDefaults = false
        }
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(get(named("jsonConfig")))
            }

            install(Logging) {
                level = LogLevel.INFO
                logger = Logger.SIMPLE
            }

            install(HttpTimeout) {
                requestTimeoutMillis = NETWORK_TIMEOUT.inWholeMilliseconds
                connectTimeoutMillis = NETWORK_TIMEOUT.inWholeMilliseconds
                socketTimeoutMillis = NETWORK_TIMEOUT.inWholeMilliseconds
            }

            install(DefaultRequest) {
                header("Accept", "application/json")
                header("Content-Type", "application/json")
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }
        }
    }

    single<ITunesApi> { ITunesApi(get()) }
}