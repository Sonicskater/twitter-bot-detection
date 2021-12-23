package features

public interface BinaryFeature {
    fun hasFeature(user: User) : Boolean
}

fun BinaryFeature.asLinear() : LinearFeature {
    return object : LinearFeature {
        override fun toString(): String {
            return this@asLinear.toString()
        }
        override fun hasFeature(user: User): Float {
            return if (this@asLinear.hasFeature(user)){
                1f
            } else {
                0f
            }
        }

        override fun name(): String {
            return this@asLinear::class.simpleName ?: ""
        }

    }
}