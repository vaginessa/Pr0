-verbose

-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

# -dontnote **
# -ignorewarnings

-dontwarn com.google.android.gms.gcm.GcmTaskService
-dontwarn android.support.transition.Transition
-dontwarn java.lang.ClassValue
-dontwarn sun.net.spi.nameservice.NameServiceDescriptor

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# keep exception names
-keepnames class * extends java.lang.Throwable

# this is for dispatching menu clicks via reflection
-keepclassmembers class * {
    @com.pr0gramm.app.ui.OnOptionsItemSelected <methods>;
}

# Keep api names for metrics
-keepnames interface com.pr0gramm.app.Api

# for gifs
-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}

# evernote android job library
-keep public class com.evernote.android.job.v21.PlatformJobService
-keep public class com.evernote.android.job.v14.PlatformAlarmService
-keep public class com.evernote.android.job.v14.PlatformAlarmReceiver
-keep public class com.evernote.android.job.JobBootReceiver
-keep public class com.evernote.android.job.JobRescheduleService


# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# keep enums!
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    **[] $VALUES;
    public *;
}

# Preserve some attributes that may be required for reflection.
-keepattributes RuntimeVisible*Annotations,InnerClasses,Signature

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep setters in Views so that animations can still work.
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# for moshi we need to keep the json adapters.
-keep,allowoptimization class **JsonAdapter {
    <init>(...);
    <fields>;
}

-keepnames @com.squareup.moshi.JsonClass class *

-keepclassmembers class **$WhenMappings {
    <fields>;
}

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
