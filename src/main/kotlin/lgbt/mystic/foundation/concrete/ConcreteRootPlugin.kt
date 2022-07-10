package lgbt.mystic.foundation.concrete

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create

class ConcreteRootPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.apply(plugin = "java")
    project.extensions.create<ConcreteExtension>("concrete")
    val setupPaperServer = project.tasks.create<SetupPaperServer>("setupPaperServer")
    val runPaperServer = project.tasks.create<RunPaperServer>("runPaperServer")
    runPaperServer.dependsOn(setupPaperServer)

    val updateManifests = project.tasks.create<UpdateManifestTask>("updateManifests")
    project.tasks.getByName("assemble").dependsOn(updateManifests)
  }
}
