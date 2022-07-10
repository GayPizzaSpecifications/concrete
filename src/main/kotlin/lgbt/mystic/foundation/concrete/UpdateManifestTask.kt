package lgbt.mystic.foundation.concrete

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Path

open class UpdateManifestTask : DefaultTask() {
  @TaskAction
  fun update() {
    val manifestsDir = ensureManifestsDir()
    val updateFile = manifestsDir.resolve("update.json")
    val rootPath = project.rootProject.rootDir.toPath()
    val updateManifest = project.findPluginProjects().mapNotNull { project ->
      val paths = project.shadowJarOutputs!!.allFilesRelativeToPath(rootPath)
      if (paths.isNotEmpty()) {
        project.name to mapOf(
          "version" to project.version,
          "artifacts" to paths.map { it.toUnixString() }
        )
      } else null
    }.toMap()

    Files.writeString(updateFile, Globals.gson.toJson(updateManifest))
  }

  private fun ensureManifestsDir(): Path {
    val manifestsDir = project.buildDir.resolve("manifests")
    manifestsDir.mkdirs()
    return manifestsDir.toPath()
  }
}
