package gay.pizza.foundation.concrete

import org.gradle.api.tasks.Internal
import java.io.File

open class RunLocalMinecraftServer : RunMinecraftServer() {
  @Internal
  lateinit var minecraftServerDirectory: File

  @Internal
  lateinit var serverJarFileName: String

  @Internal
  override fun getServerDirectory(): File = minecraftServerDirectory

  @Internal
  override fun getServerJarName(): String = serverJarFileName
}
