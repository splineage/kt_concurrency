import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

data class UserInfo(val name: String, val lastName: String, val id: Int)
lateinit var user: UserInfo

fun main() = runBlocking{
    asyncGetUserInfo(1)
    delay(1000)
    println("${Thread.activeCount()} threads active at the start")
//    val time = measureTimeMillis {
//        createCoroutines(3)
//    }
//    println("${Thread.activeCount()} threads active at the end")
//    println("Took $time ms")
    println("User ${user.id} is ${user.name}")
}

suspend fun createCoroutines(amount: Int){
    val jobs = ArrayList<Job>()
    for (i in 1..amount){
        jobs += GlobalScope.launch(Dispatchers.IO) {
            println("Started $i in ${Thread.currentThread().name}")
            delay(1000)
            println("Finished $i in ${Thread.currentThread().name}")
        }
    }
    jobs.forEach{// 모든 Job 종료 대기.ㅈ
        it.join()
    }
}

fun asyncGetUserInfo(id: Int) = GlobalScope.async {
    // delay(1100) : 추가 시 init error 발생.
    user = UserInfo(id = id, name = "JS", lastName = "Kim")
}

