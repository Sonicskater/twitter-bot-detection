import classifiers.Classifier
import classifiers.SVMClassifier
import classifiers.kNN
import features.*
import features.smu.*
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

        IsMultilingual().asLinear(), // WARNING: EXTREMELY PERFORMANCE AND MEMORY INTENSIVE!
        //MISSING: Tweet via PC,

        NameContainsSymbols().asLinear(),
        //MISSING: Pictures in tweets,
        NameContainsNumbers().asLinear(),
        //MISSING: Uses #Hashtag,
        DescHasLink().asLinear(),
        //MISSING: Videos in Tweets,
        //MISSING: Real Picture,

        HasLocation().asLinear(),
        HasProfileLocation().asLinear(),
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
        MissingID().asLinear(),
        UsingDefaultProfileImage().asLinear(),
        HasScreenName().asLinear(),
        LessThan30Followers().asLinear(),
        HasLocation().asLinear(),
        DescHasLink().asLinear(),
        LessThan50Tweets().asLinear(),
        TwoToOneFriendsToFollowers().asLinear(),
        MoreThan1kFollowers().asLinear(),
        UsingDefaultProfileImage().asLinear(),
        HasNeverTweeted().asLinear(),
        FiftyToOneFriendsToFollowers().asLinear(),
        OneHundredToOneFriendsToFollowers().asLinear(),
        HasDescription().asLinear(),
        LevenshteinDistanceLessThan30().asLinear(),
    )

    val features = SMUfeatures + JISfeatures

    val kernel = GaussianKernel(1.0/features.size)
    val SVM = SVMClassifier(kernel = kernel,features = SMUfeatures,training_data = Datasets.train.data)
    val BOTH_svm = SVMClassifier(kernel = kernel,features = features,training_data = Datasets.train.data)
    val JIS_svm = SVMClassifier(kernel = kernel,features = JISfeatures,training_data = Datasets.train.data)
    val SVM_knn = kNN(k = 50, features = SMUfeatures, training_data = Datasets.train.data)
    val BOTH_knn = kNN(k = 50, features = features, training_data = Datasets.train.data)
    val JIS_knn = kNN(k = 50, features = JISfeatures, training_data = Datasets.train.data)
    val experiments = sequenceOf(
        {runExperiment("SMU (SVM)",SVM,Datasets.dev.data)},
        {runExperiment("SMU (KNN)",SVM_knn,Datasets.dev.data)},
        {runExperiment("JIS (KNN)",JIS_knn,Datasets.dev.data)},
        {runExperiment("JIS (SVM)",JIS_svm,Datasets.dev.data)},
        {runExperiment("JIS+SMU (SVM)",BOTH_svm, Datasets.dev.data )},
        {runExperiment("JIS+SMU (KNN)",BOTH_knn, Datasets.dev.data )},
    )

    experiments.forEach {
        val e = it()
        println("${e.name}: ${e.accuracy*100}% accuracy | ${e.falsePositivePercent*100}% false positives | ${e.falseNegativePercent*100}% false negatives")
    }

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

data class ExperimentResults(
    val name: String,
    val accuracy: Double,
    val falsePositivePercent: Double,
    val falseNegativePercent: Double,
    val total: Int
)

fun runExperiment(
    name: String,
    classifier: Classifier,
    dataset: List<User>
) : ExperimentResults {
    val total = dataset.size
    var correct = 0.0
    var falseN = 0.0
    var falseP = 0.0
    dataset.map {
        val match =  classifier.classify(it).isBot() == it.isBot()
        if (match){
            correct++
        } else if (it.isBot()) {
            falseN++
        } else {
            falseP++
        }
    }

    return ExperimentResults(name, correct/total, falseP/total, falseN/total, total)
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