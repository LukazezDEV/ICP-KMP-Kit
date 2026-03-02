plugins {
    `maven-publish`
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies { implementation(libs.okio) }
        }
        val commonTest by getting

        val jvmMain by getting {
            dependencies {
                implementation(tegralLibs.niwen.lexer)
                implementation(tegralLibs.niwen.parser)
                implementation(libs.bundles.http)
                implementation(libs.guava)
                implementation(libs.bouncycastle)
                implementation(libs.logging.interceptor)
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.mockk)
                implementation(libs.bundles.junit.test)
                implementation(libs.bundles.kotlinx.test)
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.bity"
            artifactId = "icp_kotlin_kit"
            version = "1.0.1"
            from(components["kotlin"])
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        languageVersion = "1.9"
    }
}