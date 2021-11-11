import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.util.zip.*

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {

    println("ENTRIES: ${ Datasets.dev.data.size }")
    println("TWEETS: ${Datasets.dev.data.sumOf { it.tweet?.size ?: 0 } }")

    println("BOTS: ${Datasets.dev.data.count { it.isBot() }} ")

    val runtime = Runtime.getRuntime()
    val usedMemory = runtime.totalMemory()-runtime.freeMemory()

    println("USING ${usedMemory/(1024*1024)} MEGABYTES")

    val total = Datasets.dev.data.size



    val classifiers = listOf(kNN( k = 3), kNN(k = 5), kNN( k = 7))


    for (c in classifiers) {
        println(c)
        val correct = Datasets.dev.data.count { user ->
            c.classify(user).isBot() == user.isBot()
        }
        println("$correct results were correct out of $total (${correct.toDouble()/total * 100}% accuracy)")
    }


}

data class Entry(
    val z: ZipEntry,
    val data: List<User>
)