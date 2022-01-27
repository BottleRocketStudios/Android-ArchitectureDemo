# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
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

## For more info, see https://www.guardsquare.com/en/products/proguard/manual/usage#obfuscationoptions
## FIXME - VALIDATE EVERY DEV SIGNOFF: Below line must be commented out! Only uncomment to aid in debugging certain proguard related crashes to better help identify the source of the crash
#-dontobfuscate

# General proguard directives
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable,Exceptions

## Keep all generated databinding classes since some are accessed via reflection in ViewModelItem.bind
#noinspection ShrinkerUnresolvedReference
-keep class com.bottlerocketstudios.brarchitecture.databinding.** { *; }

# This fixes: Caused by: androidx.fragment.app.Fragment$InstantiationException: Unable to instantiate fragment androidx.navigation.fragment.NavHostFragment: make sure class name exists
# See https://stackoverflow.com/a/61365688/201939
-keepnames class androidx.navigation.fragment.NavHostFragment

## AndroidX Security Crypto (internal dependency on Tink)
# Crash (obfuscated): Caused by: java.lang.RuntimeException: Field keySize_ for c.b.b.a.h0.u not found. Known fields are [private int c.b.b.a.h0.u.j, private static final c.b.b.a.h0.u c.b.b.a.h0.u.k,
# private static volatile c.b.b.a.i0.a.b1 c.b.b.a.h0.u.l]
# Crash (unobfuscated): Caused by: java.lang.RuntimeException: Field version_ for com.google.crypto.tink.proto.AesGcmKeyFormat not found. Known fields are
# [private int com.google.crypto.tink.proto.AesGcmKeyFormat.keySize_, private static final com.google.crypto.tink.proto.AesGcmKeyFormat com.google.crypto.tink.proto.AesGcmKeyFormat.DEFAULT_INSTANCE,
# private static volatile com.google.crypto.tink.shaded.protobuf.Parser com.google.crypto.tink.proto.AesGcmKeyFormat.PARSER]
## Necessary as of 1.0.0-rc02. Hopefully solved in later releases. See https://issuetracker.google.com/issues/154315507#comment26 and https://stackoverflow.com/a/61485263/201939
-keepclassmembers class * extends com.google.crypto.tink.shaded.protobuf.GeneratedMessageLite {
  <fields>;
}

# Keep kotlin.Metadata annotations to maintain metadata on kept items.
# https://medium.com/androiddevelopers/shrinking-kotlin-libraries-and-applications-using-kotlin-reflection-with-r8-6fe0a0e2d115
# https://developer.android.com/studio/releases/gradle-plugin#4.1-kotlin-metadata
-keepattributes RuntimeVisibleAnnotations
-keep class kotlin.Metadata { *; }