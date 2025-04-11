package gay.pizza.foundation.concrete

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import java.nio.file.Paths

class ConcreteRootPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.apply(plugin = "base")
    project.extensions.create<ConcreteRootExtension>("concreteRoot")
    val setupPaperServer = project.tasks.register<SetupPaperServer>("setupPaperServer").get()
    val runPaperServer = project.tasks.register<RunPaperServer>("runPaperServer").get()
    runPaperServer.dependsOn(setupPaperServer)

    val maybeLocalServerPathString = project.properties["localMinecraftServerPath"]?.toString()

    if (maybeLocalServerPathString != null) {
      val localServerJarFileName = project.properties["localMinecraftServerJarFileName"]?.toString() ?: "server.jar"
      val currentWorkingDirectory = System.getProperty("user.dir")
      val localServerDirectory = Paths.get(currentWorkingDirectory).resolve(maybeLocalServerPathString).toFile()
      val setupLocalMinecraftServer = project.tasks.register<SetupLocalMinecraftServer>("setupLocalMinecraftServer").get()
      val runLocalMinecraftServer = project.tasks.register<RunLocalMinecraftServer>("runLocalMinecraftServer").get()
      runLocalMinecraftServer.dependsOn(setupLocalMinecraftServer)

      setupLocalMinecraftServer.minecraftServerDirectory = localServerDirectory
      runLocalMinecraftServer.minecraftServerDirectory = localServerDirectory
      runLocalMinecraftServer.serverJarFileName = localServerJarFileName
    }

    val updateManifests = project.tasks.register<UpdateManifestTask>("updateManifests")
    project.tasks.getByName("assemble").dependsOn(updateManifests)
  }
}
