import kotlin.math.pow

object EuclideanDistance : Distance<Float>{
    override fun distance(a: Array<Float>, b: Array<Float>): Float {
        return a.zip(b).map { (a,b) -> (a-b).pow(2) }.sum().pow(0.5f)
    }



}
