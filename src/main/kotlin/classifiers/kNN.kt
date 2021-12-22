package classifiers

import classifiers.distance.EuclideanDistance
import features.LinearFeature
import features.User

import smile.classification.knn

class kNN(
    val k : Int = 3,
    val distance : (Array<Double>, Array<Double>) -> Double = EuclideanDistance,
    training_data: List<User>,
    features : List<LinearFeature>
) : BaseClassifier(features, training_data) {

    val knn = knn(knownFeatures.map {
        it.second.toDoubleArray()
    }.toList().toTypedArray(), knownFeatures.map {
        when (it.first.isBot()) {
           true -> 1
           false -> 0
            else -> TODO()
        }
    }.toList().toIntArray(),k)

    override fun classify(user: User): Classifier.Result {
        val userFeatures = extractFeatures(user).toDoubleArray()

        val result = knn.predict(userFeatures)

        return if (result == 0){
            Classifier.Result(0.0, 1.0)
        } else {
            Classifier.Result(1.0, 0.0)
        }
    }
}
