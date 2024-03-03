package app.vercors.di

interface DIInjectionContext : DI {
    fun <T> param(index: Int = -1): T
}