import features.LinearFeature
import features.User

class kNN(
    val k : Int = 3,
    val distance : (Array<Double>, Array<Double>) -> Double = EuclideanDistance,
    training_data: List<User>,
    features : List<LinearFeature>
) : BaseClassifier(features, training_data) {

    override fun classify(user: User): Classifier.Result {
        val userFeatures = extractFeatures(user)

        val distances = knownFeatures.map { (user, features) ->

            user to distance(userFeatures, features)
        }

        val result = distances.sortedBy { it.second }.take(k)

        val bots = result.count { (user, _) -> user.isBot() }
        val real = result.count { (user, _) -> !user.isBot() }

        return Classifier.Result(bots.toDouble()/k,real.toDouble()/k)
    }
}
