package gay.pizza.foundation.concrete

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.nio.file.Files
import java.util.Properties

open class SetupPaperServer : DefaultTask() {
  init {
    outputs.upToDateWhen { false }
  }

  @get:Input
  @set:Option(option = "update", description = "Update Paper Server")
  var shouldUpdatePaperServer = true

  private val paperVersionClient = PaperVersionClient()

  @TaskAction
  fun downloadPaperTask() {
    val concrete = project.extensions.getByType<ConcreteExtension>()
    val minecraftServerDirectory = project.file(concrete.minecraftServerPath.get())

    if (!minecraftServerDirectory.exists()) {
      minecraftServerDirectory.mkdirs()
    }

    val paperJarFile = project.file("${concrete.minecraftServerPath.get()}/paper.jar")
    if (!paperJarFile.exists() || shouldUpdatePaperServer) {
      downloadLatestBuild(concrete.paperVersionGroup.get(), paperJarFile)
    }

    val paperPluginsDirectory = minecraftServerDirectory.resolve("plugins")

    if (!paperPluginsDirectory.exists()) {
      paperPluginsDirectory.mkdirs()
    }

    for (project in project.findPluginProjects()) {
      val task = project.shadowJarTask!!
      val pluginJarFile = task.outputs.files.first()
      val pluginLinkFile = paperPluginsDirectory.resolve("${project.name}.jar")
      pluginLinkFile.delete()
      Files.createSymbolicLink(pluginLinkFile.toPath(), pluginJarFile.toPath())
    }

    if (concrete.acceptServerEula.isPresent && concrete.acceptServerEula.get()) {
      val writer = minecraftServerDirectory.resolve("eula.txt").bufferedWriter()
      val properties = Properties()
      properties.setProperty("eula", "true")
      properties.store(writer, "Written by Concrete")
      writer.close()
    }
  }

  private fun downloadLatestBuild(paperVersionGroup: String, paperJarFile: File) {
    if (project.gradle.startParameter.isOffline) {
      if (!paperJarFile.exists()) {
        throw RuntimeException("Offline mode is enabled and Paper has not been downloaded.")
      } else {
        logger.lifecycle("Offline mode is enabled, skipping Paper update.")
        return
      }
    }
    val builds = paperVersionClient.getVersionBuilds(paperVersionGroup)
    val build = builds.last()
    val download = build.downloads["application"]!!
    val url = paperVersionClient.resolveDownloadUrl(build, download)
    val downloader = SmartDownloader(paperJarFile.toPath(), url, download.sha256)
    if (downloader.download()) {
      logger.lifecycle("Installed Paper Server ${build.version} build ${build.build}")
    } else {
      logger.lifecycle("Paper Server ${build.version} build ${build.build} is up-to-date")
    }
  }
}
