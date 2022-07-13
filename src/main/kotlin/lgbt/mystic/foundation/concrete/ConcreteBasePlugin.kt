package lgbt.mystic.foundation.concrete

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

open class ConcreteBasePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val versionWithBuild = if (System.getenv("CI_PIPELINE_IID") != null) {
      project.rootProject.version.toString() + ".${System.getenv("CI_PIPELINE_IID")}"
    } else {
      "DEV"
    }

    project.version = versionWithBuild

    project.plugins.apply("org.jetbrains.kotlin.jvm")
    project.plugins.apply("org.jetbrains.kotlin.plugin.serialization")

    project.extensions.getByType<JavaPluginExtension>().apply {
      val javaVersion = JavaVersion.toVersion(17)
      sourceCompatibility = javaVersion
      targetCompatibility = javaVersion
    }

    project.tasks.withType<KotlinCompile>().forEach {
      it.apply {
        kotlinOptions.apply {
          jvmTarget = "17"
        }
      }
    }
  }
}
