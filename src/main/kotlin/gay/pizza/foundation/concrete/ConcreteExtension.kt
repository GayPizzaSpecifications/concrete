package gay.pizza.foundation.concrete

import org.gradle.api.provider.Property

interface ConcreteExtension {
  val paperServerVersionGroup: Property<String>
  val paperApiVersion: Property<String>
  val minecraftServerPath: Property<String>
  val acceptServerEula: Property<Boolean>
}
