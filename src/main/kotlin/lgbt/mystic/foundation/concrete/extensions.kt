package lgbt.mystic.foundation.concrete

import org.gradle.api.Project
import org.gradle.api.tasks.TaskOutputs
import java.nio.file.FileSystems
import java.nio.file.Path

fun Project.isFoundationPlugin() = name.startsWith("foundation-")

fun Project.findPluginProjects() = rootProject.subprojects.filter { project -> project.isFoundationPlugin() }

val Project.shadowJarOutputs: TaskOutputs
  get() = project.tasks.getByName("shadowJar").outputs

fun TaskOutputs.allFilesRelativeToPath(root: Path): List<Path> = files.map { root.relativize(it.toPath()) }

fun Path.toUnixString() = toString().replace(FileSystems.getDefault().separator, "/")
