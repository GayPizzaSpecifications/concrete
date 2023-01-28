package gay.pizza.foundation.concrete

import org.gradle.api.tasks.Internal
import java.io.File

open class SetupLocalMinecraftServer : SetupMinecraftServer() {
  @Internal
  lateinit var minecraftServerDirectory: File

  @Internal
  override fun getServerDirectory(): File = minecraftServerDirectory
}
