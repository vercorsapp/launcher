-keep class app.vercors.launcher.** { *; }

-keep,allowoptimization class ch.qos.logback.classic.spi.LogbackServiceProvider { *; }
-keep,allowoptimization class kotlinx.coroutines.swing.SwingDispatcherFactory { *; }
-keep,allowoptimization class io.ktor.serialization.kotlinx.json.KotlinxSerializationJsonExtensionProvider { *; }
-keep class androidx.sqlite.driver.bundled.* { *; }
-keep,allowoptimization class org.ocpsoft.prettytime.i18n.* { *; }
-keep,allowoptimization class coil3.network.ktor3.internal.* { *; }

-dontwarn ch.qos.logback.**
-dontwarn org.koin.*.annotation.**