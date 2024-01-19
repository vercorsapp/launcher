# proguard-rules.pro

-dontwarn javax.servlet.**
-dontwarn javax.mail.**
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
-keep class com.skyecodes.vercors.** { *; }