plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.core.rate"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            afterEvaluate{
                from(components["release"])
            }
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    /*animation*/
    implementation ("com.airbnb.android:lottie:6.5.2")

    implementation ("androidx.activity:activity-ktx:1.9.2")
    implementation ("androidx.fragment:fragment-ktx:1.8.5")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.google.android.play:review-ktx:2.0.2")
}