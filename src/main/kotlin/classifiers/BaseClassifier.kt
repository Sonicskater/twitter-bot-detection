package classifiers

import features.LinearFeature
import features.User

abstract class BaseClassifier(
    val features : List<LinearFeature>,
    val training_data: List<User>
)  : Classifier {


    protected val knownFeatures = training_data.map { user ->
        user to extractFeatures(user)
    }

    protected fun extractFeatures(user: User) : Array<Double>{
        return features.map {
            it.hasFeature(user).toDouble()
        }
            .toTypedArray()
    }
}