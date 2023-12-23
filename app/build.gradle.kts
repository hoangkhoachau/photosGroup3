
plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.photosGroup3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.photosGroup3"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        mlModelBinding = true
    }


}


dependencies {
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ( "androidx.appcompat:appcompat:1.6.1" )
    implementation ( "com.google.android.material:material:1.10.0" )
    implementation ( "androidx.constraintlayout:constraintlayout:2.1.4" )
    implementation ( "androidx.navigation:navigation-fragment:2.7.5" )
    implementation ( "androidx.navigation:navigation-ui:2.7.5" )
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")

    testImplementation ( "junit:junit:4.13.2" )
    androidTestImplementation ( "androidx.test.ext:junit:1.1.5" )
    androidTestImplementation ( "androidx.test.espresso:espresso-core:3.5.1" )

    implementation ( "com.github.bumptech.glide:glide:4.12.0" )
    // Glide v4 uses this new annotation processor -- see https://bumptech.github.io/glide/doc/generatedapi.html
    implementation ("me.xdrop:fuzzywuzzy:1.2.0")
    implementation ( "androidx.viewpager2:viewpager2:1.1.0-beta02" )
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ( "com.github.yalantis:ucrop:2.2.8-native" )
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.github.Spikeysanju:ZoomRecylerLayout:1.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //noinspection GradleCompatible
    implementation ("com.android.support:recyclerview-v7:28.0.0") // Use the version that matches your library
    implementation ( "com.google.mlkit:image-labeling:17.0.7" )
    implementation ( "com.google.mlkit:text-recognition:16.0.0" )


}