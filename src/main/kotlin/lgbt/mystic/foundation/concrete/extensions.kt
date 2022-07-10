package lgbt.mystic.foundation.concrete

import org.gradle.api.Project
import org.gradle.api.tasks.TaskOutputs
import java.nio.file.FileSystems
import java.nio.file.Path

fun Project.isPluginProject() = project.tasks.names.contains("shadowJar")
fun Project.findPluginProjects() = rootProject.allprojects.filter { project -> project.isPluginProject() }

val Project.shadowJarOutputs: TaskOutputs
  get() = project.tasks.getByName("shadowJar").outputs

fun TaskOutputs.allFilesRelativeToPath(root: Path): List<Path> = files.map { root.relativize(it.toPath()) }

fun Path.toUnixString() = toString().replace(FileSystems.getDefault().separator, "/")
