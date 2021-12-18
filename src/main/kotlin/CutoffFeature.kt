import features.BinaryFeature
import features.LinearFeature
import features.User

class CutoffFeature(private val linearFeature: LinearFeature, private val cutoff: Double = 0.5) : BinaryFeature {
    override fun hasFeature(user: User): Boolean {
        return linearFeature.hasFeature(user) >= cutoff
    }
}