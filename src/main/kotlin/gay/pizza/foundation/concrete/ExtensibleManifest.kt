package gay.pizza.foundation.concrete

/**
 * The extensible update manifest format.
 */
data class ExtensibleManifest(
  /**
   * The items the manifest describes.
   */
  val items: List<ExtensibleManifestItem>
)

/**
 * An item in the update manifest.
 */
data class ExtensibleManifestItem(
  /**
   * The name of the item.
   */
  val name: String,
  /**
   * The type of item.
   */
  val type: String,
  /**
   * The version of the item.
   */
  val version: String,
  /**
   * The dependencies of the item.
   */
  val dependencies: List<String>,
  /**
   * The files that are required to install the item.
   */
  val files: List<ExtensibleManifestItemFile>
)

/**
 * A file built from the item.
 */
data class ExtensibleManifestItemFile(
  /**
   * The name of the file.
   */
  val name: String,
  /**
   * A type of file.
   */
  val type: String,
  /**
   * The relative path to download the file.
   */
  val path: String
)
