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
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

private object NetworkConfig {
    const val TIMEOUT_SECONDS = 30L
    val TIMEOUT_DURATION = TIMEOUT_SECONDS.seconds
    const val MAX_RETRIES = 3

    val jsonConfig = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
        encodeDefaults = false
    }
}

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(NetworkConfig.jsonConfig, ContentType.Application.Json)
                json(NetworkConfig.jsonConfig, ContentType.Text.JavaScript)
                json(NetworkConfig.jsonConfig, ContentType.Text.Plain)
            }

            install(Logging) {
                level = LogLevel.INFO
                logger = Logger.SIMPLE
            }

            install(HttpTimeout) {
                requestTimeoutMillis = NetworkConfig.TIMEOUT_DURATION.inWholeMilliseconds
                connectTimeoutMillis = NetworkConfig.TIMEOUT_DURATION.inWholeMilliseconds
                socketTimeoutMillis = NetworkConfig.TIMEOUT_DURATION.inWholeMilliseconds
            }

            install(DefaultRequest) {
                header(HttpHeaders.Accept, "application/json, text/javascript, text/plain")
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = NetworkConfig.MAX_RETRIES)
                exponentialDelay()
            }
        }
    }

    single<ITunesApi> { ITunesApi(get()) }
}