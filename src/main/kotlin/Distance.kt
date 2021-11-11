interface Distance<T> : (Array<T>, Array<T>) -> Float {
    fun distance(a : Array<T>, b : Array<T>) : Float

    override fun invoke(p1: Array<T>, p2: Array<T>): Float = distance(p1, p2)
}