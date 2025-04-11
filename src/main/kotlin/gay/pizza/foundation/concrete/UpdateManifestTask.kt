package gay.pizza.foundation.concrete

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path
import kotlin.io.path.relativeTo
import kotlin.io.path.writeText

open class UpdateManifestTask : DefaultTask() {
  @TaskAction
  fun update() {
    val manifestsDir = ensureManifestsDirectory()
    val rootExtension = project.concreteRootExtension
    val rootPath = if (rootExtension.expansiveItemInclusion.orNull == true) {
      project.rootProject.rootDir
    } else {
      project.rootDir
    }
    val legacyUpdateManifest = project.findPluginProjects().mapNotNull { project ->
      val paths = project.shadowJarOutputs!!.files.allFilesRelativeToPath(rootPath.toPath())

      if (paths.isNotEmpty()) {
        project.name to mapOf(
          "version" to project.version,
          "artifacts" to paths.map { it.toUnixString() }
        )
      } else null
    }.toMap()

    val legacyUpdateFile = manifestsDir.resolve("update.json")
    legacyUpdateFile.writeText(Globals.gson.toJson(legacyUpdateManifest))

    val extensibleUpdateManifestItems = project.findItemProjects().map { project ->
      val concreteItemExtension = project.concreteItemExtension!!
      val pathInclusion = concreteItemExtension.fileInclusion.orNull ?: {
        project.shadowJarOutputs!!.files.associateWith { "plugin-jar" }
      }
      val paths = pathInclusion()

      val dependencies = concreteItemExtension.dependencies.map { it.name }

      ExtensibleManifestItem(
        name = project.name,
        type = concreteItemExtension.type.orNull ?: "bukkit-plugin",
        version = concreteItemExtension.version.orNull ?: project.version.toString(),
        dependencies = dependencies,
        files = paths.map { (path, type) ->
          ExtensibleManifestItemFile(
            name = path.name,
            type = type,
            path = path.toPath().relativeTo(rootPath.toPath()).toUnixString()
          )
        }
      )
    }

    val extensibleUpdateManifest = ExtensibleManifest(
      items = extensibleUpdateManifestItems
    )

    val extensibleUpdateManifestFile = manifestsDir.resolve("manifest.json")
    extensibleUpdateManifestFile.writeText(Globals.gson.toJson(extensibleUpdateManifest) + "\n")
  }

  private fun ensureManifestsDirectory(): Path {
    val manifestsDir = project.layout.buildDirectory.asFile.get().resolve("manifests")
    manifestsDir.mkdirs()
    return manifestsDir.toPath()
  }
}
