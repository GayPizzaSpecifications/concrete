package lgbt.mystic.foundation.concrete

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project
import org.gradle.api.tasks.TaskOutputs
import java.nio.file.FileSystems
import java.nio.file.Path

internal fun Project.isPluginProject() = plugins.hasPlugin(ConcretePluginPlugin::class.java)
internal fun Project.findPluginProjects() = allprojects.filter { project -> project.isPluginProject() }

internal val Project.shadowJarTask: ShadowJar?
  get() =
    if (project.tasks.names.contains("shadowJar"))
      project.tasks.getByName("shadowJar") as ShadowJar
    else null

internal val Project.shadowJarOutputs: TaskOutputs?
  get() = shadowJarTask?.outputs

internal val Project.concreteRootExtension: ConcreteExtension
  get() {
    val direct = extensions.findByType(ConcreteExtension::class.java)
    if (direct != null) {
      return direct
    }

    if (parent != null) {
      return parent!!.concreteRootExtension
    }

    throw RuntimeException("Failed to find concrete extension. Did you apply the concrete root plugin?")
  }

internal val Project.concreteRootProject: Project
  get() {
    val direct = extensions.findByType(ConcreteExtension::class.java)
    if (direct != null) {
      return this
    }

    if (parent != null) {
      return parent!!.concreteRootProject
    }

    throw RuntimeException("Failed to find concrete root. Did you apply the concrete root plugin?")
  }

internal fun TaskOutputs.allFilesRelativeToPath(root: Path): List<Path> = files.map { root.relativize(it.toPath()) }

internal fun Path.toUnixString() = toString().replace(FileSystems.getDefault().separator, "/")
