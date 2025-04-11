package gay.pizza.foundation.concrete

import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.gradle.language.jvm.tasks.ProcessResources

class ConcretePluginPlugin : ConcreteBaseBukkitPlugin() {
  override fun apply(project: Project) {
    super.apply(project)

    project.plugins.apply("com.gradleup.shadow")

    // During IDEA project import, if this code is active, it will print warnings.
    // This will make the VERSION field unexpanded if you run using the IntelliJ build system.
    if (!project.properties.containsKey("idea.gradle.do.not.build.tasks")) {
      project.tasks.find<ProcessResources>("processResources")!!.apply {
        val props = mapOf("version" to project.version.toString())
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
          expand(props)
        }
      }
    }

    project.shadowJarTask!!.apply {
      archiveClassifier.set("plugin")
    }

    project.tasks.addTaskDependency("assemble", "shadowJar")

    project.concreteRootProject.tasks["setupPaperServer"].dependsOn(project.tasks["shadowJar"])
    project.concreteRootProject.tasks.find<SetupLocalMinecraftServer>("setupLocalMinecraftServer")
      ?.dependsOn(project.tasks["shadowJar"])
  }
}
