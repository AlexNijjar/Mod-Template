plugins {
    java
    kotlin("jvm") version "2.0.0-Beta2"
    id("org.jetbrains.intellij") version "1.16.1"
}

repositories {
    mavenCentral()
}

val intelliJVersion: String by project

intellij {
    version.set(intelliJVersion)
    type.set("IC")

    plugins.set(listOf(
        "java"
    ))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("233")
        untilBuild.set("233.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
