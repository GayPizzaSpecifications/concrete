package lgbt.mystic.foundation.concrete

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConcreteProjectPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val versionWithBuild = if (System.getenv("CI_PIPELINE_IID") != null) {
      project.rootProject.version.toString() + ".${System.getenv("CI_PIPELINE_IID")}"
    } else {
      "DEV"
    }

    project.version = versionWithBuild
  }
}
