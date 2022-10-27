plugins {
  `kotlin-dsl`
  kotlin("plugin.serialization") version "1.6.21"

  id("maven-publish")
  id("java-gradle-plugin")
}

group = "lgbt.mystic.foundation"
version = "0.5.0"

repositories {
  mavenCentral()
  gradlePluginPortal()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
  implementation("org.jetbrains.kotlin:kotlin-serialization:1.6.21")
  implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
  implementation("com.google.code.gson:gson:2.10")

  // Implementation of crypto used in smart downloader.
  implementation("org.bouncycastle:bcprov-jdk15on:1.70")
}

gradlePlugin {
  plugins {
    create("concrete-root") {
      id = "lgbt.mystic.foundation.concrete-root"
      implementationClass = "lgbt.mystic.foundation.concrete.ConcreteRootPlugin"
    }

    create("concrete-base") {
      id = "lgbt.mystic.foundation.concrete-base"
      implementationClass = "lgbt.mystic.foundation.concrete.ConcreteBasePlugin"
    }

    create("concrete-library") {
      id = "lgbt.mystic.foundation.concrete-library"
      implementationClass = "lgbt.mystic.foundation.concrete.ConcreteLibraryPlugin"
    }

    create("concrete-plugin") {
      id = "lgbt.mystic.foundation.concrete-plugin"
      implementationClass = "lgbt.mystic.foundation.concrete.ConcretePluginPlugin"
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
      name = "github-packages"
      url = uri("https://maven.pkg.github.com/mysticlgbt/concrete")
      credentials {
        username = project.findProperty("github.username") as String?
        password = project.findProperty("github.token") as String?
      }
    }

    maven {
      name = "gitlab"
      url = uri("https://gitlab.com/api/v4/projects/37752100/packages/maven")
      credentials(HttpHeaderCredentials::class.java) {
        name = "Private-Token"
        value = project.findProperty("gitlab.com.accessToken") as String?
      }

      authentication {
        create<HttpHeaderAuthentication>("header")
      }
    }
  }
}

tasks.withType<Wrapper> {
  gradleVersion = "7.5.1"
  distributionType = Wrapper.DistributionType.ALL
}
