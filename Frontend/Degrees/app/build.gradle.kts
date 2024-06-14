import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.cxx.configure.buildTypeOf
import org.gradle.api.Action

val JAVADOC_SHOW = "public"

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.degrees"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.degrees"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

project.gradle.taskGraph.whenReady {
    tasks.named<com.android.build.gradle.internal.tasks.DeviceProviderInstrumentTestTask>("connectedDebugAndroidTest") {
        ignoreFailures = true
    }
}

buildscript {
    repositories {
        maven { url = uri("http://clojars.org/repo") }
        // Other repositories if needed
    }
    // Other buildscript configurations
}

android {
    // ... other configurations

    buildTypes {
        getByName("debug") {
            isTestCoverageEnabled = true
        }

        // ... other build types
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("org.java-websocket:Java-WebSocket:1.5.4")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
}

android.applicationVariants.all(closureOf<ApplicationVariant> {
    val variant = this;
    tasks.register<Javadoc>("generate${variant.name.capitalize()}Javadoc") {
        description = "Generates Javadoc for ${variant.name}."
        source = variant.javaCompileProvider.get().source
        setDestinationDir(File("$buildDir/javadoc/"))
        isFailOnError = true

        doFirst {
            ext["androidJar"] = "${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar"
            classpath = files(variant.javaCompileProvider.get().classpath.files) + files(ext["androidJar"])
            (options as StandardJavadocDocletOptions).addStringOption("-show-members", JAVADOC_SHOW)
        }
    };
})