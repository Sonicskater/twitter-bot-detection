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
    println("ENTRIES: ${ Datasets.dev.size }")
    println("TWEETS: ${Datasets.dev.sumOf { it.tweets?.size ?: 0 } }")
    println("RETWEETS: ${Datasets.dev.sumOf { it.tweets?.filterIsInstance<Retweet>()?.size ?: 0 } }")

    println("BOTS: ${Datasets.dev.count { it.isBot() }} ")

    println("Train Dataset:")
    println("ENTRIES: ${ Datasets.train.size }")
    println("TWEETS: ${Datasets.train.sumOf { it.tweets?.size ?: 0 } }")
    println("RETWEETS: ${Datasets.train.sumOf { it.tweets?.filterIsInstance<Retweet>()?.size ?: 0 } }")

    println("BOTS: ${Datasets.train.count { it.isBot() }} ")

    val runtime = Runtime.getRuntime()
    val usedMemory = runtime.totalMemory()-runtime.freeMemory()

    println("USING ${usedMemory/(1024*1024)} MEGABYTES")

    val total = Datasets.dev.size

    // https://www.researchgate.net/profile/Ala-Al-Zoubi/publication/335026858_Spam_profiles_detection_on_social_networks_using_computational_intelligence_methods_The_effect_of_the_lingual_context/links/5d650bfd458515d610276579/Spam-profiles-detection-on-social-networks-using-computational-intelligence-methods-The-effect-of-the-lingual-context.pdf
    val JISfeatures = listOf(
        // MISSING: SUS WORDS // no list
        HighFollowingToFollowersRatio().asLinear(),
        UsingDefaultProfileImage().asLinear(),
        HighLinksToTweetRatio().asLinear(),
        //MISSING: Repeated Words,
        //MISSING: Comments Ratio,
        //MISSING: Tweet Time Pattern, //missing data
        //MISSING: Following Interest vs Tweets, //content analysis
        //MISSING: Description vs Tweets, //not sure what this means
        //MISSING: Num Tweets per Day, // missing data
        //MISSING: Uses Emotions, // Might not be possible, can't do sentiment analysis
        //MISSING: Tweets from mobile, // missing data

        IsMultilingual().asLinear(), // WARNING: EXTREMELY PERFORMANCE AND MEMORY INTENSIVE!
        //MISSING: Tweet via PC, // missing data

        NameContainsSymbols().asLinear(),
        //MISSING: Pictures in tweets, // missing data
        NameContainsNumbers().asLinear(),
        UsesHashtag().asLinear(),
        DescHasLink().asLinear(),
        //MISSING: Videos in Tweets, // missing data
        //MISSING: Real Picture, // missing data

        HasLocation().asLinear(),
        HasProfileLocation().asLinear(),
        RetweetsMoreThanTweets().asLinear(),
        LessThan100Likes().asLinear(),
        //MISSING: LINKS TO OTHER SOCIAL MEDIA,
        AgeLessThan2Months().asLinear(),
        MoreThan100Followers().asLinear(),
        //MISSING: Trending Topics, // requires topic analysis

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

    val bothFeatures = SMUfeatures + JISfeatures

    val allFeatures = bothFeatures + EmojiUsage()

    val kernel = GaussianKernel(1.0/bothFeatures.size)

    val experiments = sequenceOf(
        {
            val SVM = SVMClassifier(kernel = kernel,features = SMUfeatures,training_data = Datasets.train)
            runExperiment("SMU (SVM)",SVM,Datasets.dev)
        },
//        {
//            val SVM_knn = kNN(k = 50, features = SMUfeatures, training_data = Datasets.train)
//            runExperiment("SMU (KNN)",SVM_knn,Datasets.dev)
//        },
        {
            val JIS_knn = kNN(k = 50, features = JISfeatures, training_data = Datasets.train)
            runExperiment("JIS (KNN)",JIS_knn,Datasets.dev)
        },
//        {
//            val JIS_svm = SVMClassifier(kernel = kernel,features = JISfeatures,training_data = Datasets.train)
//            runExperiment("JIS (SVM)",JIS_svm,Datasets.dev)
//        },
//        {
//
//            val BOTH_svm = SVMClassifier(kernel = kernel,features = bothFeatures,training_data = Datasets.train)
//            runExperiment("JIS+SMU (SVM)",BOTH_svm, Datasets.dev)
//        },
//        {
//            val BOTH_knn = kNN(k = 50, features = bothFeatures, training_data = Datasets.train)
//            runExperiment("JIS+SMU (KNN)",BOTH_knn, Datasets.dev)
//        },
        {
            val all_svm = SVMClassifier(kernel = kernel,features = allFeatures,training_data = Datasets.train)
            runExperiment("ALL (SVM)",all_svm, Datasets.dev)
        },
        {
            val all_knn = kNN(k = 50, features = allFeatures, training_data = Datasets.train)
            runExperiment("ALL (KNN)",all_knn, Datasets.dev)
        },
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