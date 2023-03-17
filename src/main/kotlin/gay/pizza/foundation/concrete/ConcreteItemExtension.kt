package gay.pizza.foundation.concrete

import org.gradle.api.DomainObjectSet
import org.gradle.api.Project
import org.gradle.api.provider.Property
import java.io.File

interface ConcreteItemExtension {
  val type: Property<String>
  val version: Property<String>
  val dependencies: DomainObjectSet<Project>
  val fileInclusion: Property<() -> Map<File, String>>

  fun dependency(project: Project) {
    dependencies.add(project)
  }

  fun fileInclusion(inclusion: () -> Map<File, String>) {
    fileInclusion.set(inclusion)
  }
}
