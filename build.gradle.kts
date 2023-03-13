@file:Suppress("UnstableApiUsage")
plugins {
  `kotlin-dsl`

  `maven-publish`
  `java-gradle-plugin`

  id("com.gradle.plugin-publish") version "1.1.0"
}

group = "gay.pizza.foundation"
version = "0.13.0"

repositories {
  mavenCentral()
  gradlePluginPortal()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
  implementation("org.jetbrains.kotlin:kotlin-serialization:1.8.10")
  implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
  implementation("com.google.code.gson:gson:2.10.1")

  // Implementation of crypto used in smart downloader.
  implementation("org.bouncycastle:bcprov-jdk15on:1.70")
}

gradlePlugin {
  website.set("https://github.com/GayPizzaSpecifications/concrete")
  vcsUrl.set("https://github.com/GayPizzaSpecifications/concrete")

  plugins {
    create("concrete-root") {
      id = "gay.pizza.foundation.concrete-root"
      implementationClass = "gay.pizza.foundation.concrete.ConcreteRootPlugin"

      displayName = "Concrete Root"
      description = "Gradle conventions for Foundation Bukkit plugins. Root project plugin."
    }

    create("concrete-base") {
      id = "gay.pizza.foundation.concrete-base"
      implementationClass = "gay.pizza.foundation.concrete.ConcreteBasePlugin"

      displayName = "Concrete Base"
      description = "Gradle conventions for Foundation Bukkit plugins. Base project plugin."
    }

    create("concrete-library") {
      id = "gay.pizza.foundation.concrete-library"
      implementationClass = "gay.pizza.foundation.concrete.ConcreteLibraryPlugin"

      displayName = "Concrete Library"
      description = "Gradle conventions for Foundation Bukkit plugins. Library project plugin."
    }

    create("concrete-plugin") {
      id = "gay.pizza.foundation.concrete-plugin"
      implementationClass = "gay.pizza.foundation.concrete.ConcretePluginPlugin"

      displayName = "Concrete Library"
      description = "Gradle conventions for Foundation Bukkit plugins. Plugin project plugin."
    }

    forEach { declaration ->
      declaration.tags.set(listOf("foundation-concrete", "minecraft-bukkit"))
    }
  }
}

val gradlePublishingKey: String? = System.getenv("GRADLE_PLUGIN_PUBLISHING_KEY")
val gradlePublishingSecret: String? = System.getenv("GRADLE_PLUGIN_PUBLISHING_SECRET")

if (gradlePublishingKey != null && gradlePublishingSecret != null) {
  project.setProperty("gradle.publish.key", gradlePublishingKey.toString())
  project.setProperty("gradle.publish.secret", gradlePublishingSecret.toString())
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

    var githubPackagesToken = System.getenv("GITHUB_TOKEN")
    if (githubPackagesToken == null) {
      githubPackagesToken = project.findProperty("github.token") as String?
    }

    var gitlabPackagesToken = System.getenv("GITLAB_TOKEN")
    if (gitlabPackagesToken == null) {
      gitlabPackagesToken = project.findProperty("gitlab.com.accessToken") as String?
    }

    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/gaypizzaspecifications/concrete")
      credentials {
        username = project.findProperty("github.username") as String? ?: "gaypizzaspecifications"
        password = githubPackagesToken
      }
    }

    maven {
      name = "GitLab"
      url = uri("https://gitlab.com/api/v4/projects/42873094/packages/maven")
      credentials(HttpHeaderCredentials::class.java) {
        name = "Private-Token"
        value = gitlabPackagesToken
      }

      authentication {
        create<HttpHeaderAuthentication>("header")
      }
    }
  }
}

tasks.withType<Wrapper> {
  gradleVersion = "8.0"
}

java {
  withSourcesJar()
}
