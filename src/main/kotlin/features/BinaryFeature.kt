package features

import User

public interface BinaryFeature {
    fun hasFeature(user: User) : Boolean
}

fun BinaryFeature.asLinear() : LinearFeature {
    return object : LinearFeature {
        override fun hasFeature(user: User): Float {
            return if (this@asLinear.hasFeature(user)){
                1f
            } else {
                0f
            }
        }

    }
}