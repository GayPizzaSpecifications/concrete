package gay.pizza.foundation.concrete

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import java.nio.file.Paths

class ConcreteRootPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.apply(plugin = "base")
    project.extensions.create<ConcreteExtension>("concrete")
    val setupPaperServer = project.tasks.create<SetupPaperServer>("setupPaperServer")
    val runPaperServer = project.tasks.create<RunPaperServer>("runPaperServer")
    runPaperServer.dependsOn(setupPaperServer)

    val maybeLocalServerPathString = project.properties["localMinecraftServerPath"]?.toString()

    if (maybeLocalServerPathString != null) {
      val localServerJarFileName = project.properties["localMinecraftServerJarFileName"]?.toString() ?: "server.jar"
      val currentWorkingDirectory = System.getProperty("user.dir")
      val localServerDirectory = Paths.get(currentWorkingDirectory).resolve(maybeLocalServerPathString).toFile()
      val setupLocalMinecraftServer = project.tasks.create<SetupLocalMinecraftServer>("setupLocalMinecraftServer")
      val runLocalMinecraftServer = project.tasks.create<RunLocalMinecraftServer>("runLocalMinecraftServer")
      runLocalMinecraftServer.dependsOn(setupLocalMinecraftServer)

      setupLocalMinecraftServer.minecraftServerDirectory = localServerDirectory
      runLocalMinecraftServer.minecraftServerDirectory = localServerDirectory
      runLocalMinecraftServer.serverJarFileName = localServerJarFileName
    }

    val updateManifests = project.tasks.create<UpdateManifestTask>("updateManifests")
    project.tasks.getByName("assemble").dependsOn(updateManifests)
  }
}
