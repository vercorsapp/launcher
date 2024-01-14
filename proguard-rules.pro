# proguard-rules.pro

-dontwarn javax.servlet.**
-dontwarn javax.mail.**
-dontwarn org.codehaus.**

-keepclasseswithmembers public class com.skyecodes.vercors.MainKt {
    public static void main(java.lang.String[]);
}