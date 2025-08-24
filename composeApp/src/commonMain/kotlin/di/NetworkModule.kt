package di

import data.remote.ITunesApi
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

private object NetworkDefaults {
    const val TIMEOUT_SECONDS = 30L
    val TIMEOUT_DURATION = TIMEOUT_SECONDS.seconds
    const val MAX_RETRIES = 3
}

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
                json(get(named("jsonConfig")), ContentType.Application.Json)
                json(get(named("jsonConfig")), ContentType.Text.JavaScript)
            }

            install(Logging) {
                level = LogLevel.INFO
                logger = Logger.SIMPLE
            }

            install(HttpTimeout) {
                requestTimeoutMillis = NetworkDefaults.TIMEOUT_DURATION.inWholeMilliseconds
                connectTimeoutMillis = NetworkDefaults.TIMEOUT_DURATION.inWholeMilliseconds
                socketTimeoutMillis = NetworkDefaults.TIMEOUT_DURATION.inWholeMilliseconds
            }

            install(DefaultRequest) {
                header("Accept", "application/json, text/javascript")
                header("Content-Type", "application/json")
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = NetworkDefaults.MAX_RETRIES)
                exponentialDelay()
            }
        }
    }

    single<ITunesApi> { ITunesApi(get()) }
}