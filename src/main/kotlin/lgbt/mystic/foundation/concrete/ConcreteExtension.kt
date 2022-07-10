package lgbt.mystic.foundation.concrete

import org.gradle.api.provider.Property

interface ConcreteExtension {
  val paperVersionGroup: Property<String>
  val minecraftServerPath: Property<String>
}
