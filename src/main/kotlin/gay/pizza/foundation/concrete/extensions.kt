package gay.pizza.foundation.concrete

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskOutputs
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Path

/**
 * Checks if the project has the [ConcretePluginPlugin] applied.
 */
internal fun Project.isPluginProject() = plugins.hasPlugin(ConcretePluginPlugin::class.java)

/**
 * Checks if the project has the [ConcreteBasePlugin] applied and is opting into item behavior.
 */
internal fun Project.isConcreteItem() =
  isPluginProject() || concreteItemExtension?.type?.orNull != null

/**
 * Finds all projects in the project's hierarchy that are plugins.
 */
internal fun Project.findPluginProjects() = allprojects.filter { project -> project.isPluginProject() }

/**
 * Finds all projects in the project's hierarchy that are items.
 */
internal fun Project.findItemProjects(): List<Project> {
  val optInExpansion = concreteRootExtension.expansiveItemInclusion.orNull ?: false
  val searchScope = if (optInExpansion) {
    project.rootProject.allprojects
  } else {
    allprojects
  }
  return searchScope.filter { project -> project.isConcreteItem() }
}

internal fun TaskContainer.addTaskDependency(dependent: String, dependency: String) {
  getByName(dependent).dependsOn(getByName(dependency))
}

internal inline fun <reified T> TaskContainer.find(name: String) =
  findByName(name) as T?

internal val Project.shadowJarTask: ShadowJar?
  get() = project.tasks.find<ShadowJar>("shadowJar")

internal val Project.shadowJarOutputs: TaskOutputs?
  get() = shadowJarTask?.outputs

internal val Project.concreteRootExtension: ConcreteRootExtension
  get() = findTargetParent(
    valid = { extensions.findByType(ConcreteRootExtension::class.java) != null },
    extract = { extensions.findByType(ConcreteRootExtension::class.java)!! },
    error = { "Failed to find concrete root. Did you apply the concrete root plugin?" }
  )

internal val Project.concreteItemExtension: ConcreteItemExtension?
  get() = extensions.findByType(ConcreteItemExtension::class.java)

/**
 * Finds the concrete root project, which is the first project in the project hierarchy
 * that has the concrete extension.
 */
internal val Project.concreteRootProject: Project
  get() = findTargetParent(
    valid = { extensions.findByType(ConcreteRootExtension::class.java) != null },
    extract = { this },
    error = { "Failed to find concrete root. Did you apply the concrete root plugin?" }
  )

/**
 * Scans a project hierarchy looking for a project matching the criteria specified in [valid].
 * If found, [extract] is called and the result is returned. If no matching project is found, [error] is called
 * and passed to RuntimeException as an error string.
 */
internal fun <T> Project.findTargetParent(valid: Project.() -> Boolean, extract: Project.() -> T, error: () -> String): T {
  if (valid(this)) {
    return extract(this)
  }

  if (parent != null) {
    return parent!!.findTargetParent(valid, extract, error)
  }

  throw RuntimeException(error())
}

internal fun Iterable<File>.allFilesRelativeToPath(root: Path): List<Path> = map { root.relativize(it.toPath()) }

internal fun Path.toUnixString() = toString().replace(FileSystems.getDefault().separator, "/")
