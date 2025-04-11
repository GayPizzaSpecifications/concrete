package gay.pizza.foundation.concrete

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import java.io.File
import java.util.jar.JarFile
import javax.inject.Inject

abstract class RunMinecraftServer @Inject constructor(private var execOperations: ExecOperations) : DefaultTask() {
  init {
    outputs.upToDateWhen { false }
  }

  @get:Input
  var additionalServerArguments = mutableListOf<String>()

  @get:Input
  var disableServerGui = true

  @TaskAction
  fun runMinecraftServer() {
    val minecraftServerDirectory = getServerDirectory()
    val serverJarFile = minecraftServerDirectory.resolve(getServerJarName())
    val mainClassName = readMainClass(serverJarFile)

    execOperations.javaexec {
      classpath(serverJarFile.absolutePath)
      workingDir(minecraftServerDirectory)

      val allServerArguments = mutableListOf<String>()
      allServerArguments.addAll(additionalServerArguments)
      if (disableServerGui) {
        allServerArguments.add("nogui")
      }

      args(allServerArguments)
      mainClass.set(mainClassName)
    }
  }

  private fun readMainClass(file: File): String = JarFile(file).use { jar ->
    jar.manifest.mainAttributes.getValue("Main-Class")!!
  }

  @Internal
  abstract fun getServerDirectory(): File

  @Internal
  abstract fun getServerJarName(): String
}
