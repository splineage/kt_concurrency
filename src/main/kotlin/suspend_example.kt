import kotlinx.coroutines.*
import model.Profile

@OptIn(DelicateCoroutinesApi::class)
suspend fun suspendExample() {
    val dispatcher = newFixedThreadPoolContext(4,"myThread")
    val handlerException = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error in $coroutineContext")
        println("Message : ${throwable.message}")
    }

    GlobalScope.launch(handlerException){
        println("Thread ${Thread.currentThread()}")
        greetDelayed(1000)
        val client: ProfileServiceRepository = ProfileServiceClient()
        val profile = client.fetchById(12)
        println(profile)
        TODO("Not implemented yet")
    }.join()
}

suspend fun greetDelayed(delayMillis: Long){
    println("Thread ${Thread.currentThread()}")
    delay(delayMillis)
    println("Hello")
}

interface ProfileServiceRepository{
    suspend fun fetchByName(name: String): Profile
    suspend fun fetchById(id: Long): Profile
}

class ProfileServiceClient: ProfileServiceRepository{
    override suspend fun fetchByName(name: String): Profile{
        return Profile(1, name, 30)
    }

    override suspend fun fetchById(id: Long): Profile{
        delay(1000)
        return Profile(id, "Kim", 30)
    }
}