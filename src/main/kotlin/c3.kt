import kotlinx.coroutines.*

@OptIn(InternalCoroutinesApi::class, DelicateCoroutinesApi::class)
fun main() = runBlocking{
    /**
     * handler
     */
//    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
//        println("Job cancelled due to ${throwable.message}")
//    }
//    val job = GlobalScope.launch(exceptionHandler) {
//        throw Exception()
//    }
//    delay(2000)

    /**
     * cancellationException
      */
//    job.cancel()
//    job.cancel(cause = CancellationException("Timeout!"))
//    val cancellation = job.getCancellationException()
//    println(cancellation.message)

    /**
     * invokeOnCompletion
     */
    val invokeJob = GlobalScope.launch {
        throw Exception("not implement")
    }.invokeOnCompletion { cause ->
        cause?.let {
            println("${it.message}")
        }
    }
    delay(2000)
}