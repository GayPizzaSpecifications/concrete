package lgbt.mystic.foundation.concrete

import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.gradle.language.jvm.tasks.ProcessResources

@Suppress("UnstableApiUsage")
class ConcretePluginPlugin : ConcreteProjectPlugin() {
  override fun apply(project: Project) {
    super.apply(project)

    project.plugins.apply("com.github.johnrengelman.shadow")

    (project.tasks.getByName("processResources") as ProcessResources).apply {
      val props = mapOf("version" to project.version.toString())
      inputs.properties(props)
      filteringCharset = "UTF-8"
      filesMatching("plugin.yml") {
        expand(props)
      }
    }

    project.shadowJarTask!!.apply {
      archiveClassifier.set("plugin")
    }

    project.tasks["assemble"].dependsOn(project.tasks["shadowJar"])

    project.concreteRootProject.tasks["setupPaperServer"].dependsOn(project.tasks["shadowJar"])
  }
}
