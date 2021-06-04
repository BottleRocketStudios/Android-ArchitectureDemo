# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep kotlin.Metadata annotations to maintain metadata on kept items.
# https://medium.com/androiddevelopers/shrinking-kotlin-libraries-and-applications-using-kotlin-reflection-with-r8-6fe0a0e2d115
# https://developer.android.com/studio/releases/gradle-plugin#4.1-kotlin-metadata
-keepattributes RuntimeVisibleAnnotations
-keep class kotlin.Metadata { *; }