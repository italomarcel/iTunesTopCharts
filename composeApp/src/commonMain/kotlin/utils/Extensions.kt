package utils

import kotlinx.coroutines.CancellationException

suspend inline fun <R> runSuspendCatching(block: suspend () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (cancellationException: CancellationException) {
        throw cancellationException
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
