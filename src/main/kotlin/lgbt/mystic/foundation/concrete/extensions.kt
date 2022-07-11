package lgbt.mystic.foundation.concrete

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskOutputs
import java.nio.file.FileSystems
import java.nio.file.Path

internal fun Project.isPluginProject() = plugins.hasPlugin(ConcretePluginPlugin::class.java)
internal fun Project.findPluginProjects() = allprojects.filter { project -> project.isPluginProject() }

internal inline fun <reified T> TaskContainer.find(name: String) =
  findByName(name) as T?

internal val Project.shadowJarTask: ShadowJar?
  get() = project.tasks.find<ShadowJar>("shadowJar")

internal val Project.shadowJarOutputs: TaskOutputs?
  get() = shadowJarTask?.outputs

internal val Project.concreteRootExtension: ConcreteExtension
  get() = findTargetParent(
    valid = { extensions.findByType(ConcreteExtension::class.java) != null },
    extract = { extensions.findByType(ConcreteExtension::class.java)!! },
    error = { "Failed to find concrete root. Did you apply the concrete root plugin?" }
  )

internal val Project.concreteRootProject: Project
  get() = findTargetParent(
    valid = { extensions.findByType(ConcreteExtension::class.java) != null },
    extract = { this },
    error = { "Failed to find concrete root. Did you apply the concrete root plugin?" }
  )

internal fun <T> Project.findTargetParent(valid: Project.() -> Boolean, extract: Project.() -> T, error: () -> String): T {
  if (valid(this)) {
    return extract(this)
  }

  if (parent != null) {
    return parent!!.findTargetParent(valid, extract, error)
  }

  throw RuntimeException(error())
}

internal fun TaskOutputs.allFilesRelativeToPath(root: Path): List<Path> = files.map { root.relativize(it.toPath()) }

internal fun Path.toUnixString() = toString().replace(FileSystems.getDefault().separator, "/")
