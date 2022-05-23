import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
fun main_deferred() = runBlocking {
    val deferred = GlobalScope.async {
        TODO("Not implemented yet!")
    }
//    delay(2000)
    /**
     * Exception handle
     */
    try {
        deferred.await() // exception 전파.
    }catch (e: Throwable){
        println(e.message)
    }


}