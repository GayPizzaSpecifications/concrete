package gay.pizza.foundation.concrete

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.nio.file.Files
import java.util.Properties

open class SetupPaperServer : SetupMinecraftServer() {
  init {
    outputs.upToDateWhen { false }
  }

  @get:Input
  @set:Option(option = "update", description = "Update Paper Server")
  var shouldUpdatePaperServer = true

  private val paperVersionClient = PaperVersionClient()

  @TaskAction
  fun setupPaperServer() {
    val concrete = project.extensions.getByType<ConcreteExtension>()
    val minecraftServerDirectory = getServerDirectory()
    val paperJarFile = project.file("${minecraftServerDirectory}/paper.jar")
    if (!paperJarFile.exists() || shouldUpdatePaperServer) {
      downloadLatestBuild(concrete.paperServerVersionGroup.get(), paperJarFile)
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

  @Internal
  override fun getServerDirectory(): File {
    val concrete = project.extensions.getByType<ConcreteExtension>()
    return project.file(concrete.minecraftServerPath.get())
  }
}
