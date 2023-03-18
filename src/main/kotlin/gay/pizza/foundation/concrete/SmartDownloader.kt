package gay.pizza.foundation.concrete

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.exists
import kotlin.io.path.inputStream

class SmartDownloader(
  private val localFilePath: Path,
  private val remoteDownloadUrl: URI,
  private val sha256: String
) {
  fun download(): Boolean {
    val hashResult = checkLocalFileHash()
    if (hashResult != HashResult.ValidHash) {
      downloadRemoteFile()
      return true
    }
    return false
  }

  private fun downloadRemoteFile() {
    val httpClient = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
      .GET()
      .uri(remoteDownloadUrl)
      .build()
    httpClient.send(request, BodyHandlers.ofFile(localFilePath))
    val hashResult = checkLocalFileHash()
    if (hashResult != HashResult.ValidHash) {
      throw RuntimeException("Download of $remoteDownloadUrl did not result in a valid hash.")
    }
  }

  private fun checkLocalFileHash(): HashResult {
    if (!localFilePath.exists()) {
      return HashResult.DoesNotExist
    }

    val digest = MessageDigest.getInstance("SHA-256")
    val localFileStream = localFilePath.inputStream()
    val buffer = ByteArray(16 * 1024)

    while (true) {
      val size = localFileStream.read(buffer)
      if (size <= 0) {
        break
      }

      val bytes = buffer.take(size).toByteArray()
      digest.update(bytes)
    }

    val sha256Bytes = digest.digest()
    val localSha256Hash = bytesToHex(sha256Bytes)
    return if (localSha256Hash.equals(sha256, ignoreCase = true)) {
      HashResult.ValidHash
    } else {
      HashResult.InvalidHash
    }
  }

  private fun bytesToHex(hash: ByteArray): String {
    val hexString = StringBuilder(2 * hash.size)
    for (i in hash.indices) {
      val hex = Integer.toHexString(0xff and hash[i].toInt())
      if (hex.length == 1) {
        hexString.append('0')
      }
      hexString.append(hex)
    }
    return hexString.toString()
  }

  enum class HashResult {
    DoesNotExist,
    InvalidHash,
    ValidHash
  }
}
