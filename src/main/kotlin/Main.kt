import classifiers.SVMClassifier
import classifiers.kNN
import features.*
import features.DescHasLink
import features.jis.*
import features.smu.LessThan30Followers
import features.smu.LevenshteinDistanceLessThan30
import kotlinx.serialization.ExperimentalSerializationApi
import smile.math.kernel.GaussianKernel
import java.util.zip.*
import kotlin.math.pow

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {
    println("Dev Dataset:")
    println("ENTRIES: ${ Datasets.dev.data.size }")
    println("TWEETS: ${Datasets.dev.data.sumOf { it.tweets?.size ?: 0 } }")
    println("RETWEETS: ${Datasets.dev.data.sumOf { it.tweets?.filterIsInstance<Retweet>()?.size ?: 0 } }")

    println("BOTS: ${Datasets.dev.data.count { it.isBot() }} ")

    println("Train Dataset:")
    println("ENTRIES: ${ Datasets.train.data.size }")
    println("TWEETS: ${Datasets.train.data.sumOf { it.tweets?.size ?: 0 } }")
    println("RETWEETS: ${Datasets.train.data.sumOf { it.tweets?.filterIsInstance<Retweet>()?.size ?: 0 } }")

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

    // https://www.researchgate.net/profile/Ala-Al-Zoubi/publication/335026858_Spam_profiles_detection_on_social_networks_using_computational_intelligence_methods_The_effect_of_the_lingual_context/links/5d650bfd458515d610276579/Spam-profiles-detection-on-social-networks-using-computational-intelligence-methods-The-effect-of-the-lingual-context.pdf
    val JISfeatures = listOf(
        // MISSING: SUS WORDS
        HighFollowingToFollowersRatio().asLinear(),
        UsingDefaultProfileImage().asLinear(),
        //MISSING: high Text-to-links ratio,
        //MISSING: Repeated Words,
        //MISSING: Comments Ratio,
        //MISSING: Tweet Time Pattern,
        //MISSING: Follwing Interest vs Tweets,
        //MISSING: Description vs Tweets,
        //MISSING: Num Tweets per Day,
        //MISSING: Uses Emoticons,
        //MISSING: Tweets from mobile,
        //IsMultilingual().asLinear(),
        //MISSING: Tweet via PC,
        //MISSING: Use Symbols in name,
        //MISSING: Pictures in tweets,
        //MISSING: Useres Numbers in name,
        //MISSING: Uses #Hashtag,
        DescHasLink().asLinear(),
        //MISSING: Videos in Tweets,
        //MISSING: Real Picture,
        //MISSING: Living Place,
        RetweetsMoreThanTweets().asLinear(),
        LessThan100Likes().asLinear(),
        //MISSING: LINKS TO OTHER SOCIAL MEDIA,
        AgeLessThan2Months().asLinear(),
        MoreThan100Followers().asLinear(),
        //MISSING: Trending Topics,
        MoreThan50Following().asLinear()
    )

    //https://scholar.smu.edu/cgi/viewcontent.cgi?article=1019&context=datasciencereview
    val SMUfeatures = listOf(
        // MISSING: Absence of ID
        // MISSING: No profile picture ,
        //MISSING: HAS SCREEN NAME
        LessThan30Followers().asLinear(),
        HasLocation().asLinear(),
        //MISSING: LANG NOT ENG
        DescHasLink().asLinear(),
        // MISSING: LESS THAN 50 TWEETS,
        // MISSING 2:1 friends/followers,
        // MISSING: Over 1k followers,
        UsingDefaultProfileImage().asLinear(),
        // MISSING: Has Never Tweeted,
        // MISSING 50:1 freinds:followers
        // MISSING 100:1 freinds:followers
        HasDescription().asLinear(),
        LevenshteinDistanceLessThan30().asLinear(),
    )

    val kernel = GaussianKernel(1.0)
    val SVM = SVMClassifier(kernel,SMUfeatures,Datasets.train.data)

    val svm_correct = Datasets.dev.data.count { user ->
        SVM.classify(user).isBot() == user.isBot()
    }
    println("SMU (SVM): $svm_correct results were correct out of $total (${svm_correct.toDouble() / total * 100}% accuracy)")

    val JIS_knn = kNN(k = 50, features = JISfeatures, training_data = Datasets.train.data)

    val jis_correct = Datasets.dev.data.count { user ->
        JIS_knn.classify(user).isBot() == user.isBot()
    }
    println("JIS (KNN): $jis_correct results were correct out of $total (${jis_correct.toDouble() / total * 100}% accuracy)")


//    val classifiers = listOf(
//        classifiers.kNN( k = 3, features = features),
//        classifiers.kNN(k = 5, features = features),
//        classifiers.kNN( k = 7, features = features),
//        classifiers.kNN(k = 50, features = features),
//        classifiers.kNN(k = 100, features = features),
//        classifiers.kNN(k = 150, features = features),
//        classifiers.kNN(k = 300, features = features)
//    )

//    val max : Double = getAllCombos(features).asStream().parallel().map {
//        if (it.isEmpty()){
//            0.0
//        } else{
//            val c = classifiers.kNN(k = 50, features = it)
//            println(c)
//            val correct = Datasets.dev.data.count { user ->
//                c.classify(user).isBot() == user.isBot()
//            }
//            println("$correct results were correct out of $total (${correct.toDouble() / total * 100}% accuracy)")
//            correct.toDouble() / total * 100
//        }
//    }.max(Double::compareTo).get()
//
//    println("Best Percentage found: $max")


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