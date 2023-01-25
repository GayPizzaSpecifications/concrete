package gay.pizza.foundation.concrete

import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.gradle.language.jvm.tasks.ProcessResources

@Suppress("UnstableApiUsage")
class ConcretePluginPlugin : ConcreteBaseBukkitPlugin() {
  override fun apply(project: Project) {
    super.apply(project)

    project.plugins.apply("com.github.johnrengelman.shadow")

    project.tasks.find<ProcessResources>("processResources")!!.apply {
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

    project.tasks.addTaskDependency("assemble", "shadowJar")

    project.concreteRootProject.tasks["setupPaperServer"].dependsOn(project.tasks["shadowJar"])
  }
}
