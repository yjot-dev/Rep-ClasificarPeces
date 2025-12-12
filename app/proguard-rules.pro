# Add project specific ProGuard rules here.
# Reglas para TensorFlow Lite y AutoValue
-dontwarn com.google.auto.value.**
-keep class com.google.auto.value.** { *; }
-dontwarn org.tensorflow.lite.**