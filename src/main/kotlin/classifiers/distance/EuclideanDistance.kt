package classifiers.distance

import org.apache.commons.math3.ml.distance.EuclideanDistance

object EuclideanDistance : Distance<Double> {
    override fun distance(a: Array<Double>, b: Array<Double>): Double{
        return EuclideanDistance().compute(a.toDoubleArray(),b.toDoubleArray())
    }
}
