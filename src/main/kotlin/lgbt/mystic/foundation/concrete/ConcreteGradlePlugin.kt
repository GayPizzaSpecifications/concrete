package lgbt.mystic.foundation.concrete

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class ConcreteGradlePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.extensions.create<ConcreteExtension>("concrete")
    val setupPaperServer = project.tasks.create<SetupPaperServer>("setupPaperServer")
    project.afterEvaluate { ->
      setupPaperServer.dependsOn(*project.subprojects
        // TODO: Foundation specific
        .filter { it.isFoundationPlugin() }
        .map { it.tasks.getByName("shadowJar") }
        .toTypedArray()
      )
    }
    val runPaperServer = project.tasks.create<RunPaperServer>("runPaperServer")
    runPaperServer.dependsOn(setupPaperServer)

    val updateManifests = project.tasks.create<UpdateManifestTask>("updateManifests")
    project.tasks.getByName("assemble").dependsOn(updateManifests)
  }
}
