package gay.pizza.foundation.concrete

import org.gradle.api.tasks.Internal
import org.gradle.kotlin.dsl.getByType
import java.io.File

open class RunPaperServer : RunMinecraftServer() {
  init {
    outputs.upToDateWhen { false }
  }

  @Internal
  override fun getServerDirectory(): File {
    val concrete = project.extensions.getByType<ConcreteRootExtension>()
    return project.file(concrete.minecraftServerPath.get())
  }

  @Internal
  override fun getServerJarName(): String = "paper.jar"
}
