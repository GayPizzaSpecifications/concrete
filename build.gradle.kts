plugins {
  `kotlin-dsl`
  kotlin("plugin.serialization") version "1.6.21"

  id("maven-publish")
  id("java-gradle-plugin")
}

group = "lgbt.mystic.foundation"
version = "0.1.0"

repositories {
  mavenCentral()
  gradlePluginPortal()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
  implementation("org.jetbrains.kotlin:kotlin-serialization:1.6.21")
  implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
  implementation("com.google.code.gson:gson:2.9.0")

  // TODO: What uses this?
  implementation("org.bouncycastle:bcprov-jdk15on:1.70")
}

gradlePlugin {
  plugins {
    create("concrete") {
      id = "lgbt.mystic.foundation.concrete"
      implementationClass = "lgbt.mystic.foundation.concrete.ConcreteGradlePlugin"
    }
  }
}

publishing {
  repositories {
    mavenLocal()
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/mysticlgbt/concrete")
      credentials {
        username = project.findProperty("github.username") as String?
        password = project.findProperty("github.token") as String?
      }
    }
  }
}
