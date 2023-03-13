package gay.pizza.foundation.concrete

import org.gradle.api.DomainObjectSet
import org.gradle.api.Project

interface ConcretePluginExtension {
  val dependencies: DomainObjectSet<Project>

  fun dependency(project: Project) {
    dependencies.add(project)
  }
}
