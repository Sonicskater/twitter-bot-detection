package features

class ListingCount : LinearFeature {
    override fun hasFeature(user: User): Float {
        val count = user.profile?.listed_count?.trim()?.toInt() ?: 0
        return count.toFloat() / 1000
    }

    override fun name(): String {
        return this::class.simpleName!!
    }
}