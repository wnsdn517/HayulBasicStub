plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "dev.seola.apppatcher.stub"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.seola.apppatcher.stub"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            multiDexEnabled = false
        }
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
}

dependencies {
}

android.applicationVariants.all {
    val variant = this
    val outputPath = "${rootProject.rootDir.path}/output"

    variant.assembleProvider.configure {
        doLast {
            copy {
                variant.outputs.forEach { output ->
                    val file =
                        zipTree(file(output.outputFile)).matching { include("classes*.dex") }.singleFile

                    from(file)
                    into(outputPath)
                    rename { fileName ->
                        fileName.replace(file.name, "patcher.dex")
                    }
                }
            }
        }
    }
}