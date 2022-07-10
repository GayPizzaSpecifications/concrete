package lgbt.mystic.foundation.concrete

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.util.jar.JarFile

open class RunPaperServer : DefaultTask() {
  init {
    outputs.upToDateWhen { false }
  }

  @TaskAction
  fun runPaperServer() {
    val concrete = project.extensions.getByType<ConcreteExtension>()

    val minecraftServerDirectory = project.file(concrete.minecraftServerPath.get())
    val paperJarFile = minecraftServerDirectory.resolve("paper.jar")
    val mainClassName = readMainClass(paperJarFile)

    project.javaexec {
      classpath(paperJarFile.absolutePath)
      workingDir(minecraftServerDirectory)
      args("nogui")
      mainClass.set(mainClassName)
    }
  }

  private fun readMainClass(file: File): String = JarFile(file).use { jar ->
    jar.manifest.mainAttributes.getValue("Main-Class")!!
  }
}
