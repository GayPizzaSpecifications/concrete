package gay.pizza.foundation.concrete

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name

open class UpdateManifestTask : DefaultTask() {
  @TaskAction
  fun update() {
    val manifestsDir = ensureManifestsDirectory()
    val rootPath = project.projectDir.toPath()
    val legacyUpdateManifest = project.findPluginProjects().mapNotNull { project ->
      val paths = project.shadowJarOutputs!!.allFilesRelativeToPath(rootPath)

      if (paths.isNotEmpty()) {
        project.name to mapOf(
          "version" to project.version,
          "artifacts" to paths.map { it.toUnixString() }
        )
      } else null
    }.toMap()

    val legacyUpdateFile = manifestsDir.resolve("update.json")
    Files.writeString(legacyUpdateFile, Globals.gson.toJson(legacyUpdateManifest))

    val extensibleUpdateManifestItems = project.findPluginProjects().mapNotNull { project ->
      val paths = project.shadowJarOutputs!!.allFilesRelativeToPath(rootPath)

      val dependencies = project.concretePluginExtension.dependencies.map { it.name }
      ExtensibleManifestItem(
        name = project.name,
        type = "bukkit-plugin",
        version = project.version.toString(),
        dependencies = dependencies,
        files = paths.map { path ->
          var type = "unknown"
          if (path.name.endsWith("-plugin.jar")) {
            type = "plugin-jar"
          }
          ExtensibleManifestItemFile(
            name = path.name,
            type = type,
            path = path.toUnixString()
          )
        }
      )
    }

    val extensibleUpdateManifest = ExtensibleManifest(
      items = extensibleUpdateManifestItems
    )

    val extensibleUpdateManifestFile = manifestsDir.resolve("manifest.json")
    Files.writeString(
      extensibleUpdateManifestFile, Globals.gsonPretty.toJson(extensibleUpdateManifest) + "\n")
  }

  private fun ensureManifestsDirectory(): Path {
    val manifestsDir = project.buildDir.resolve("manifests")
    manifestsDir.mkdirs()
    return manifestsDir.toPath()
  }
}
