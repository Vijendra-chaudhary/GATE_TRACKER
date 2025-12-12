# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\thevi\AppData\Local\Android\Sdk/tools/proguard/proguard-android-optimize.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# Keep all data models used in Backup/Restore to prevent JSON key renaming
-keep class com.gate.tracker.data.model.** { *; }

# Keep Room entities to ensure database schema compatibility
-keep class com.gate.tracker.data.local.entity.** { *; }

# Keep generic Gson serializable classes if any
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Keep Retrofit/Network models if any (Good practice for future)
#-keep class com.gate.tracker.data.remote.** { *; }
