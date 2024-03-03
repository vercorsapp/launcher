package app.vercors.di

class DefaultDIInjectionContext(di: DI, private val args: Array<out Any>) : DIInjectionContext, DI by di {
    private var indexBuffer = 0

    @Suppress("unchecked_cast")
    override fun <T> param(index: Int): T {
        val i = if (index < 0) indexBuffer++ else index
        return args[i] as T
    }

}