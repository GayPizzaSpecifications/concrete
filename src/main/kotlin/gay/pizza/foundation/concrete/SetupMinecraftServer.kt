package gay.pizza.foundation.concrete

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.nio.file.Files
import java.util.*

abstract class SetupMinecraftServer : DefaultTask() {
  init {
    outputs.upToDateWhen { false }
  }

  @TaskAction
  fun setupMinecraftAction() {
    val minecraftServerDirectory = getServerDirectory()
    if (!minecraftServerDirectory.exists()) {
      minecraftServerDirectory.mkdirs()
    }

    val serverPluginsDirectory = minecraftServerDirectory.resolve("plugins")

    if (!serverPluginsDirectory.exists()) {
      serverPluginsDirectory.mkdirs()
    }

    for (project in project.findPluginProjects()) {
      val task = project.shadowJarTask!!
      val pluginJarFile = task.outputs.files.first()
      val pluginLinkFile = serverPluginsDirectory.resolve("${project.name}.jar")
      pluginLinkFile.delete()
      Files.createSymbolicLink(pluginLinkFile.toPath(), pluginJarFile.toPath())
    }

    val concrete = project.extensions.getByType<ConcreteRootExtension>()
    if (concrete.acceptServerEula.isPresent && concrete.acceptServerEula.get()) {
      val writer = minecraftServerDirectory.resolve("eula.txt").bufferedWriter()
      val properties = Properties()
      properties.setProperty("eula", "true")
      properties.store(writer, "Written by Concrete")
      writer.close()
    }
  }

  @Internal
  abstract fun getServerDirectory(): File
}
