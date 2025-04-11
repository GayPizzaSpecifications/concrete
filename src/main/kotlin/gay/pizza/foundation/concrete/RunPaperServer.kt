package gay.pizza.foundation.concrete

import org.gradle.api.tasks.Internal
import org.gradle.kotlin.dsl.getByType
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

open class RunPaperServer @Inject constructor(execOperations: ExecOperations) : RunMinecraftServer(execOperations) {
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
