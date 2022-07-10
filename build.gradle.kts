plugins {
  `kotlin-dsl`
  kotlin("plugin.serialization") version "1.6.21"

  id("maven-publish")
  id("java-gradle-plugin")
}

group = "lgbt.mystic.foundation"
version = "0.3.0"

repositories {
  mavenCentral()
  gradlePluginPortal()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
  implementation("org.jetbrains.kotlin:kotlin-serialization:1.6.21")
  implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
  implementation("com.google.code.gson:gson:2.9.0")

  // Implementation of crypto used in smart downloader.
  implementation("org.bouncycastle:bcprov-jdk15on:1.70")
}

gradlePlugin {
  plugins {
    create("concrete-root") {
      id = "lgbt.mystic.foundation.concrete-root"
      implementationClass = "lgbt.mystic.foundation.concrete.ConcreteRootPlugin"
    }

    create("concrete-plugin") {
      id = "lgbt.mystic.foundation.concrete-plugin"
      implementationClass = "lgbt.mystic.foundation.concrete.ConcreteProjectPlugin"
    }
  }
}

java {
  val version = JavaVersion.toVersion("11")
  sourceCompatibility = version
  targetCompatibility = version
}

tasks.compileKotlin {
  kotlinOptions {
    jvmTarget = "11"
  }
}

publishing {
  repositories {
    mavenLocal()
    maven {
      name = "github-packages"
      url = uri("https://maven.pkg.github.com/mysticlgbt/concrete")
      credentials {
        username = project.findProperty("github.username") as String?
        password = project.findProperty("github.token") as String?
      }
    }
  }
}
