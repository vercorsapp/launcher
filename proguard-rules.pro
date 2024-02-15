# proguard-rules.pro

-dontwarn javax.servlet.**
-dontwarn javax.mail.**
-dontwarn androidx.compose.material3.**
-dontwarn org.codehaus.**
-dontwarn io.github.g00fy2.versioncompare.**

-keep class com.sun.jna.** { *; }
-keep class net.harawata.appdirs.** { *; }
-keep class com.jthemedetecor.** { *; }
-keep class oshi.** { *; }
-keep class kotlinx.coroutines.swing.SwingDispatcherFactory { *; }
-keep class ch.qos.logback.classic.spi.LogbackServiceProvider { *; }
-keep class org.ocpsoft.prettytime.** { *; }
-keep class io.ktor.serialization.kotlinx.json.KotlinxSerializationJsonExtensionProvider { *; }
-keep class com.arkivanov.decompose.extensions.compose.mainthread.SwingMainThreadChecker { *; }
-keep class com.skyecodes.vercors.** { *; }
-keep class javax.naming.** { *; }

# When editing this file, update the following files as well:
# - META-INF/com.android.tools/proguard/coroutines.pro
# - META-INF/com.android.tools/r8/coroutines.pro

# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

# These classes are only required by kotlinx.coroutines.debug.AgentPremain, which is only loaded when
# kotlinx-coroutines-core is used as a Java agent, so these are not needed in contexts where ProGuard is used.
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler
-dontwarn java.lang.instrument.Instrumentation
-dontwarn sun.misc.Signal

# Only used in `kotlinx.coroutines.internal.ExceptionsConstructor`.
# The case when it is not available is hidden in a `try`-`catch`, as well as a check for Android.
-dontwarn java.lang.ClassValue

# An annotation used for build tooling, won't be directly accessed.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement