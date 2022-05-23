import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.newSingleThreadContext

suspend fun iterator_example() {
    val iterator = iterator<Any> {
        yield("First")
        yield("Second")
        yield("Third")
        yield(1)
        yield(10L)
    }
    iterator.forEachRemaining{
        println(it)
    }

    println("-----------------")

    val sequence = sequence<Any> {
        yield(1)
        yield("A")
        yield(32L)
    }

    sequence.forEach {
        println(it)
    }

    sequence.forEachIndexed{index, any ->
        println("$index $any")
    }

    fibo_sequence()

    val producer = GlobalScope.produce {
        // return ReceiveChannel<E>
        for (i in 0..9){
            send(i)
        }
    }
    println( producer.receive())
    println( producer.receive())

    println("--------------------")
    fibo_producer()

}

fun fibo_sequence(){
    val fibo = sequence<Long> {
        yield(1)
        var current = 1L
        var next = 1L
        while (true){
            yield(next)
            val tmpNext = current + next
            current = next
            next = tmpNext
        }
    }
    val indexed = fibo.take(50).withIndex()

    for ((index, value) in indexed){
        println("$index: $value")
    }
}

suspend fun fibo_producer(){
    val context = newSingleThreadContext("producer Thread")
    val fibonacci = GlobalScope.produce(context) {
        send(1L)
        var current = 1L
        var next = 1L
        var count = 0
        while (count <10){
            send(next)
            val tmpNext = current + next
            current = next
            next = tmpNext
            count++
        }
    }
    fibonacci.consumeEach{
        println(it)
    }
}