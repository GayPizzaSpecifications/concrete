package lgbt.mystic.foundation.concrete

import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PaperVersionClient(
  val client: HttpClient = HttpClient.newHttpClient(),
  private val gson: Gson = Globals.gson
) {
  private val apiBaseUrl = URI.create("https://papermc.io/api/v2/")

  fun getVersionBuilds(group: String): List<PaperBuild> {
    val response = client.send(
      HttpRequest.newBuilder()
        .GET()
        .uri(apiBaseUrl.resolve("projects/paper/version_group/${group}/builds"))
        .build(),
      HttpResponse.BodyHandlers.ofString()
    )

    val body = response.body()
    val root = gson.fromJson(body, PaperVersionRoot::class.java)
    return root.builds
  }

  fun resolveDownloadUrl(build: PaperBuild, download: PaperVersionDownload): URI =
    apiBaseUrl.resolve("projects/paper/versions/${build.version}/builds/${build.build}/downloads/${download.name}")

  data class PaperVersionRoot(
    val builds: List<PaperBuild>
  )

  data class PaperBuild(
    val version: String,
    val build: Int,
    val downloads: Map<String, PaperVersionDownload>
  )

  data class PaperVersionDownload(
    val name: String,
    val sha256: String
  )
}
