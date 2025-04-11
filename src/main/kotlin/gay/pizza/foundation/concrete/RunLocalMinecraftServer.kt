package gay.pizza.foundation.concrete

import org.gradle.api.tasks.Internal
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

open class RunLocalMinecraftServer @Inject constructor(execOperations: ExecOperations) : RunMinecraftServer(execOperations) {
  @Internal
  lateinit var minecraftServerDirectory: File

  @Internal
  lateinit var serverJarFileName: String

  @Internal
  override fun getServerDirectory(): File = minecraftServerDirectory

  @Internal
  override fun getServerJarName(): String = serverJarFileName
}
