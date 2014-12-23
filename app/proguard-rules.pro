# Add project specific ProGuard rules here.
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-keepattributes SourceFile,LineNumberTable

# Add any project specific keep options here:
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.Fragment

-keep class * implements android.os.Parcelable {
    public static android.os.Parcelable$Creator *;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

# jdk
-keep class javax.inject.** { *; }
-keep class javax.annotation.** { *; }

# Butterknife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *; }

# RxJava
-dontwarn rx.internal.util.**

# Dagger
-dontwarn com.google.auto.**
-dontwarn com.google.common.**
-dontwarn com.squareup.javawriter.**
-dontwarn dagger.internal.codegen.**
-keepclassmembers class * {
	@com.google.inject.Inject <init>(...);
	@com.google.inject.Inject <fields>;
	@javax.annotation.Nullable <fields>;
}