plugins {
  `kotlin-dsl`
  kotlin("plugin.serialization") version "1.7.10"

  id("maven-publish")
  id("java-gradle-plugin")
}

group = "gay.pizza.foundation"
version = "0.6.0-SNAPSHOT"

repositories {
  mavenCentral()
  gradlePluginPortal()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
  implementation("org.jetbrains.kotlin:kotlin-serialization:1.7.10")
  implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
  implementation("com.google.code.gson:gson:2.10.1")

  // Implementation of crypto used in smart downloader.
  implementation("org.bouncycastle:bcprov-jdk15on:1.70")
}

gradlePlugin {
  plugins {
    create("concrete-root") {
      id = "gay.pizza.foundation.concrete-root"
      implementationClass = "gay.pizza.foundation.concrete.ConcreteRootPlugin"
    }

    create("concrete-base") {
      id = "gay.pizza.foundation.concrete-base"
      implementationClass = "gay.pizza.foundation.concrete.ConcreteBasePlugin"
    }

    create("concrete-library") {
      id = "gay.pizza.foundation.concrete-library"
      implementationClass = "gay.pizza.foundation.concrete.ConcreteLibraryPlugin"
    }

    create("concrete-plugin") {
      id = "gay.pizza.foundation.concrete-plugin"
      implementationClass = "gay.pizza.foundation.concrete.ConcretePluginPlugin"
    }
  }
}

java {
  val version = JavaVersion.toVersion("17")
  sourceCompatibility = version
  targetCompatibility = version
}

tasks.compileKotlin {
  kotlinOptions {
    jvmTarget = "17"
  }
}

publishing {
  repositories {
    mavenLocal()

    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/gaypizzaspecifications/concrete")
      credentials {
        username = project.findProperty("github.username") as String? ?: "unknown"
        password = project.findProperty("github.token") as String? ?: System.getenv("GITHUB_TOKEN")
      }
    }
  }
}

tasks.withType<Wrapper> {
  gradleVersion = "7.6"
  distributionType = Wrapper.DistributionType.ALL
}
