package lgbt.mystic.foundation.concrete

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

@Suppress("UnstableApiUsage")
class ConcreteProjectPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val versionWithBuild = if (System.getenv("CI_PIPELINE_IID") != null) {
      project.rootProject.version.toString() + ".${System.getenv("CI_PIPELINE_IID")}"
    } else {
      "DEV"
    }

    project.plugins.apply("org.jetbrains.kotlin.jvm")
    project.plugins.apply("org.jetbrains.kotlin.plugin.serialization")
    project.plugins.apply("com.github.johnrengelman.shadow")

    project.version = versionWithBuild

    project.repositories {
      maven {
        name = "papermc"
        url = URI.create("https://papermc.io/repo/repository/maven-public/")
      }
    }

    val paperApiVersion = project.rootProject.extensions.getByType<ConcreteExtension>().paperApiVersion.get()

    project.dependencies.add("compileOnly", "io.papermc.paper:paper-api:${paperApiVersion}")

    project.extensions.getByType<JavaPluginExtension>().apply {
      val javaVersion = JavaVersion.toVersion(17)
      sourceCompatibility = javaVersion
      targetCompatibility = javaVersion
    }

    (project.tasks.getByName("processResources") as ProcessResources).apply {
      val props = mapOf("version" to project.version.toString())
      inputs.properties(props)
      filteringCharset = "UTF-8"
      filesMatching("plugin.yml") {
        expand(props)
      }
    }

    (project.tasks["shadowJar"] as ShadowJar).apply {
      archiveClassifier.set("plugin")
    }

    project.tasks["assemble"].dependsOn(project.tasks["shadowJar"])

    project.tasks.withType<KotlinCompile>().forEach {
      it.apply {
        kotlinOptions.apply {
          jvmTarget = "17"
        }
      }
    }

    project.rootProject.tasks["setupPaperServer"].dependsOn(project.tasks["shadowJar"])
  }
}
