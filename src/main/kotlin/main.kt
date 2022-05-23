import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.system.measureTimeMillis

data class UserInfo(val name: String, val lastName: String, val id: Int)
lateinit var user: UserInfo

fun main(): Unit = runBlocking{
//    asyncGetUserInfo(1)
//    delay(1000)
//    println("${Thread.activeCount()} threads active at the start")
//    val time = measureTimeMillis {
//        createCoroutines(3)
//    }
//    println("${Thread.activeCount()} threads active at the end")
//    println("Took $time ms")
//    println("User ${user.id} is ${user.name}")

//    context_example_async()

//    iterator_example()

    /**
     * channel
     */
    var time = measureTimeMillis {
        val channel = Channel<Int>()
        val sender = GlobalScope.launch {
            repeat(10){
                channel.send(it)
                println("Send $it")
            }
        }
        channel.receive()
        channel.receive()

    }
    println("channel Took $time")

    time = measureTimeMillis {
        val unBufferedChannel = Channel<Int>(Channel.UNLIMITED) // 송신자 중지 안함.
        val sender = GlobalScope.launch {
            repeat(5){
                println("Sending $it")
                unBufferedChannel.send(it)
            }
        }
        delay(500)
    }
    println("unbufferedchannel took $time")

    time = measureTimeMillis {
        val channel = Channel<Int>(4) // 버퍼 크기에 의해 중단됨.
        val sender = GlobalScope.launch {
            repeat(10){
                channel.send(it)
                println("Send $it")
            }
        }
        delay(500)
        println("Taking two")
        channel.receive()
        delay(500)
    }
    println("unbufferedchannel took $time")

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

