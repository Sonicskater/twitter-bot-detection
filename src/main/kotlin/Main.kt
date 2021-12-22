import classifiers.Classifier
import classifiers.SVMClassifier
import features.*
import features.smu.*
import features.DescHasLink
import features.jis.*
import features.smu.LessThan30Followers
import features.smu.LevenshteinDistanceLessThan30
import kotlinx.serialization.ExperimentalSerializationApi
import smile.feature.FeatureRanking
import smile.feature.SignalNoiseRatio
import smile.feature.SumSquaresRatio
import java.util.zip.*
import kotlin.math.pow

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {


    println("Dev Dataset:")
    println("ENTRIES: ${ Datasets.dev.size }")
    println("TWEETS: ${Datasets.dev.sumOf { it.tweets?.size ?: 0 } }")
    println("RETWEETS: ${Datasets.dev.sumOf { it.tweets?.filterIsInstance<Retweet>()?.size ?: 0 } }")

    println("BOTS: ${Datasets.dev.count { it.isBot() ?: false }} ")

    println("Train Dataset:")
    println("ENTRIES: ${ Datasets.train.size }")
    println("TWEETS: ${Datasets.train.sumOf { it.tweets?.size ?: 0 } }")
    println("RETWEETS: ${Datasets.train.sumOf { it.tweets?.filterIsInstance<Retweet>()?.size ?: 0 } }")

    println("BOTS: ${Datasets.train.count { it.isBot() ?: false }} ")



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
        CommentsRatio(),
        //MISSING: Tweet Time Pattern, //missing data
        //MISSING: Following Interest vs Tweets, //content analysis
        //MISSING: Description vs Tweets, //not sure what this means
        //MISSING: Num Tweets per Day, // missing data
        //MISSING: Uses Emotions, // Might not be possible, can't do sentiment analysis
        IsMultilingual().asLinear(), // WARNING: EXTREMELY PERFORMANCE AND MEMORY INTENSIVE!
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

    val newFeatures = listOf(
        EmojiUsage(),
        UserIsVerified().asLinear(),
        ProfileUsesBackgroundImage().asLinear(),
        BusinessDomain().asLinear(),
        EntertainmentDomain().asLinear(),
        PoliticsDomain().asLinear(),
        SportsDomain().asLinear(),
        DomainCount(),
        ListingCount()
    )

    val bothFeatures = SMUfeatures + JISfeatures

    val allFeatures = bothFeatures + newFeatures

    val optimized = optimizedFeatures(allFeatures, Datasets.train, cutoff = 0.05)
    val optimizedSignal = optimizedFeatures(allFeatures, Datasets.train, cutoff = 0.1, featureRanker = SignalNoiseRatio())

    //val kernel = GaussianKernel(1.0/bothFeatures.size)

    val experiments = sequenceOf(
//        {
//
//            val SVM = SVMClassifier(features = SMUfeatures,training_data = Datasets.train)
//            runExperiment("SMU (SVM)",SVM,Datasets.dev)
//        },
//        {
//            val SVM_knn = kNN(k = 50, features = SMUfeatures, training_data = Datasets.train)
//            runExperiment("SMU (KNN)",SVM_knn,Datasets.dev)
//        },
//        {
//            val JIS_knn = kNN(k = 50, features = JISfeatures, training_data = Datasets.train)
//            runExperiment("JIS (KNN)",JIS_knn,Datasets.dev)
//        },
//        {
//            val JIS_svm = SVMClassifier(features = JISfeatures,training_data = Datasets.train)
//            runExperiment("JIS (SVM)",JIS_svm,Datasets.dev)
//        },
//        {
//
//            val BOTH_svm = SVMClassifier(features = bothFeatures,training_data = Datasets.train)
//            runExperiment("JIS+SMU (SVM)",BOTH_svm, Datasets.dev)
//        },
//        {
//            val BOTH_knn = kNN(k = 50, features = bothFeatures, training_data = Datasets.train)
//            runExperiment("JIS+SMU (KNN)",BOTH_knn, Datasets.dev)
//        },
//        {
//            val all_svm = SVMClassifier(features = allFeatures,training_data = Datasets.train)
//            runExperiment("ALL (SVM)",all_svm, Datasets.dev)
//        },
//        {
//            val all_knn = kNN(k = 50, features = allFeatures, training_data = Datasets.train)
//            runExperiment("ALL (KNN)",all_knn, Datasets.dev)
//        },
        {
            val optimized_svm = SVMClassifier(features = optimized,training_data = Datasets.train)
            runExperiment("Optimized (SVM) Dev Data",optimized_svm, Datasets.dev)
        },
//        {
//            val optimized_knn = kNN(k = 50, features = optimized, training_data = Datasets.train)
//            runExperiment("Optimized (KNN) Dev Data",optimized_knn, Datasets.dev)
//        },
        {
            val optimized_svm = SVMClassifier(features = optimized,training_data = Datasets.train)
            runExperiment("Optimized (SVM) Test Data",optimized_svm, Datasets.test)
        },
        {
            val optimized_svm = SVMClassifier(features = optimized,training_data = Datasets.train)
            runExperiment("Optimized (SVM) Train Data",optimized_svm, Datasets.train)
        },
        {
            val optimized_svm = SVMClassifier(features = optimizedSignal,training_data = Datasets.train)
            runExperiment("Optimized Signal to Noise (SVM) Dev Data",optimized_svm, Datasets.dev)
        },
//        {
//            val optimized_knn = kNN(k = 50, features = optimized, training_data = Datasets.train)
//            runExperiment("Optimized (KNN) Dev Data",optimized_knn, Datasets.dev)
//        },
        {
            val optimized_svm = SVMClassifier(features = optimizedSignal,training_data = Datasets.train)
            runExperiment("Optimized Signal to Noise (SVM) Test Data",optimized_svm, Datasets.test)
        },
        {
            val optimized_svm = SVMClassifier(features = optimizedSignal,training_data = Datasets.train)
            runExperiment("Optimized Signal to Noise (SVM) Train Data",optimized_svm, Datasets.train)
        },
//        {
//            val optimized_svm = SVMClassifier(features = optimized,training_data = Datasets.train)
//            runExperiment("Optimized (SVM) Support Data",optimized_svm, Datasets.support)
//        },
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

fun optimizedFeatures(features: List<LinearFeature>, dataset: Dataset, cutoff: Double = 0.1, featureRanker : FeatureRanking = SumSquaresRatio()) : List<LinearFeature>{

//    val numberedFeatures = features.withIndex().associate {
//        it.index to it.value
//    }

    val data = dataset.map { u ->
        features.map {
            it.hasFeature(u).toDouble()
        }.toDoubleArray()
    }.toTypedArray()

    val labels = dataset.map {
        when (it.isBot()!!){
            true -> 1 // is bot
            false -> -1 // is not bot
        }
    }.toIntArray()

    val featureEffectiveness = featureRanker.rank(data, labels)

    val result = features.zip(featureEffectiveness.toList())

    println("Feature Effectiveness: $result")

    val selectedFeatures = result.filter {
        it.second>=cutoff
    }.map {
        it.first
    }

    println("Selected Features: $selectedFeatures")

    return selectedFeatures
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
    dataset.filter {
        it.isBot() != null
    }.map {
        val isUserBot = it.isBot()!!
        val match =  classifier.classify(it).isBot() == isUserBot
        if (match){
            correct++
        } else if (isUserBot) {
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