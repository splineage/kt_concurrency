import kotlinx.coroutines.*

suspend fun context_example(){
    val dispatcher = newSingleThreadContext("myDispatcher")
    val handler = CoroutineExceptionHandler { _, throwable ->
        println("myDispatcher Coroutine Error")
        println("Message : ${throwable.message}")
    }
    val context = dispatcher + handler
    val tmpCtx = context.minusKey(dispatcher.key)
    GlobalScope.launch(tmpCtx) {
        delay(1000)
        println("running in ${Thread.currentThread().name}")
    }.join()

}

suspend fun context_example_async(){
    val dispatcher = newSingleThreadContext("myThread")
//    val name = GlobalScope.async(dispatcher) {
//        delay(1000)
//        "Kim"
//    }.await()
    val name = withContext(dispatcher){
        // async 는 await() 호출 필요. Deferred<T>
        // withContext 결과리턴 대기. <T>
        "Kim"
    }
    println("User : $name")
}