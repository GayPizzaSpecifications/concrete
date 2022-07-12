package lgbt.mystic.foundation.concrete

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.util.jar.JarFile

open class RunPaperServer : DefaultTask() {
  init {
    outputs.upToDateWhen { false }
  }

  @get:Input
  var additionalServerArguments = mutableListOf<String>()

  @get:Input
  var disableServerGui = true

  @TaskAction
  fun runPaperServer() {
    val concrete = project.extensions.getByType<ConcreteExtension>()

    val minecraftServerDirectory = project.file(concrete.minecraftServerPath.get())
    val paperJarFile = minecraftServerDirectory.resolve("paper.jar")
    val mainClassName = readMainClass(paperJarFile)

    project.javaexec {
      classpath(paperJarFile.absolutePath)
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
}
