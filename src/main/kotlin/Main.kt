import kotlinx.serialization.ExperimentalSerializationApi
import java.util.zip.*
import kotlin.math.pow
import kotlin.streams.asStream

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {
    println("Dev Dataset:")
    println("ENTRIES: ${ Datasets.dev.data.size }")
    println("TWEETS: ${Datasets.dev.data.sumOf { it.tweet?.size ?: 0 } }")

    println("BOTS: ${Datasets.dev.data.count { it.isBot() }} ")

    println("Train Dataset:")
    println("ENTRIES: ${ Datasets.train.data.size }")
    println("TWEETS: ${Datasets.train.data.sumOf { it.tweet?.size ?: 0 } }")

    println("BOTS: ${Datasets.train.data.count { it.isBot() }} ")

    val runtime = Runtime.getRuntime()
    val usedMemory = runtime.totalMemory()-runtime.freeMemory()

    println("USING ${usedMemory/(1024*1024)} MEGABYTES")

    val total = Datasets.dev.data.size

    val features : List<LinearFeature> = listOf(
        AgeLessThan2Months().asLinear(),
        HasDescription().asLinear(),
        HasLocation().asLinear(),
        HasProfileLocation().asLinear(),
        HighFollowingToFollowersRatio().asLinear(),
        LessThan30Followers().asLinear(),
        LevenshteinDistanceLessThan30().asLinear(),
        LessThan100Likes().asLinear(),
        MoreThan50Following().asLinear(),
        MoreThan100Followers().asLinear(),
        ProfileUsesBackgroundImage().asLinear(),
        UserIsVerified().asLinear(),
        UsesExtendedProfile().asLinear(),
        UsingDefaultProfile().asLinear(),
        UsingDefaultProfileImage().asLinear(),
    )

//    val classifiers = listOf(
//        kNN( k = 3, features = features),
//        kNN(k = 5, features = features),
//        kNN( k = 7, features = features),
//        kNN(k = 50, features = features),
//        kNN(k = 100, features = features),
//        kNN(k = 150, features = features),
//        kNN(k = 300, features = features)
//    )

    val max : Double = getAllCombos(features).asStream().parallel().map {
        if (it.isEmpty()){
            0.0
        } else{
            val c = kNN(k = 50, features = it)
            println(c)
            val correct = Datasets.dev.data.count { user ->
                c.classify(user).isBot() == user.isBot()
            }
            println("$correct results were correct out of $total (${correct.toDouble() / total * 100}% accuracy)")
            correct.toDouble() / total * 100
        }
    }.max(Double::compareTo).get()

    println("Best Percentage found: $max")


}

data class Entry(
    val z: ZipEntry,
    val data: List<User>
)

fun <T> getAllCombos(input: List<T>) : Sequence<List<T>>{
    val count : Int = (2.0.pow(input.size)-1).toInt()

    // Sequence evaluates lazily, saves memory
    return sequence<List<T>> {
        for (i in 1..count){
            val result = mutableListOf<T>()

            for (j in 0..input.size){
                if ((i shr j).mod(2) != 0){
                    result.add(input[j])
                }
            }
            yield(result)
        }
    }
}