import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    application
    java
}

group = "com.db.twitter"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/Sonicskater/twitter-bot-detection")
        credentials {
            username = "Sonicskater"
            password = "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"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1-fixed_streams")
    implementation("org.apache.commons:commons-text:1.9")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.jetbrains.kotlinx:kotlin-deeplearning-api:0.3.0")

    implementation("com.github.haifengl:smile-kotlin:2.6.0")
}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaExec>(){
    jvmArgs = listOf("-Xms8g","-Xmx8g")
}

application {
    mainClass.set("MainKt")
}