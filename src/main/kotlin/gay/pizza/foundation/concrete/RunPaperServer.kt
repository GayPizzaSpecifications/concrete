package gay.pizza.foundation.concrete

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.util.jar.JarFile

open class RunPaperServer : RunMinecraftServer() {
  init {
    outputs.upToDateWhen { false }
  }

  @Internal
  override fun getServerDirectory(): File {
    val concrete = project.extensions.getByType<ConcreteExtension>()
    return project.file(concrete.minecraftServerPath.get())
  }

  @Internal
  override fun getServerJarName(): String = "paper.jar"
}
