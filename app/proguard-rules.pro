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

# General proguard directives
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable,Exceptions

## Keep all generated databinding classes since some are accessed via reflection in ViewModelItem.bind
#noinspection ShrinkerUnresolvedReference
-keep class com.bottlerocketstudios.brarchitecture.databinding.** { *; }

## Keep all ViewModel classes since they are accessed via reflection in BitbucketApplication.BitbucketViewModelFactory.create
-keep class * extends androidx.lifecycle.AndroidViewModel { *; }
-keep class * extends com.bottlerocketstudios.brarchitecture.ui.BaseViewModel { *; }
-keep class * extends com.bottlerocketstudios.brarchitecture.ui.ScopedViewModel { *; }
-keep class * extends com.bottlerocketstudios.brarchitecture.ui.RepoViewModel { *; }

# This fixes: Caused by: androidx.fragment.app.Fragment$InstantiationException: Unable to instantiate fragment androidx.navigation.fragment.NavHostFragment: make sure class name exists
# See https://stackoverflow.com/a/61365688/201939
-keepnames class androidx.navigation.fragment.NavHostFragment