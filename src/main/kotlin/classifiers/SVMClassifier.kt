package classifiers

import Datasets
import features.LinearFeature
import features.User
import smile.classification.svm
import smile.math.kernel.GaussianKernel
import smile.math.kernel.MercerKernel

class SVMClassifier(
    features: List<LinearFeature>,
    training_data: List<User>
) : BaseClassifier(features, training_data) {

    val kernel: MercerKernel<DoubleArray> = GaussianKernel(1.0/features.size)

    val smu_labels_vector = training_data.map {
        when (it.isBot()!!){
            true -> 1 // is bot
            false -> -1 // is not bot
        }
    }.toIntArray()

    val trainingData = training_data.toTypedArray().map {
        extractFeatures(it).toDoubleArray()
    }.toTypedArray()

    val svm = svm(
        trainingData,
        smu_labels_vector,
        kernel,
        0.5// from https://scholar.smu.edu/cgi/viewcontent.cgi?article=1019&context=datasciencereview
    )

    override fun classify(user: User): Classifier.Result {
        val x = extractFeatures(user).toDoubleArray()
        val clazz = svm.predict(x)
        return when (clazz){
            -1 -> Classifier.Result(bot = 0.0, not_bot = 1.0)
            1 -> Classifier.Result(bot = 1.0, not_bot = 0.0)
            else -> TODO("Unknown classification")
        }
    }
}