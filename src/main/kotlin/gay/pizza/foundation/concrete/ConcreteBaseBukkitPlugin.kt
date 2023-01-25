package gay.pizza.foundation.concrete

import org.gradle.api.Project
import org.gradle.kotlin.dsl.repositories
import java.net.URI

open class ConcreteBaseBukkitPlugin : ConcreteBasePlugin() {
  override fun apply(project: Project) {
    super.apply(project)

    project.repositories {
      maven {
        name = "papermc"
        url = URI.create("https://papermc.io/repo/repository/maven-public/")
      }
    }

    project.afterEvaluate {
      val paperApiVersion = project.concreteRootExtension.paperApiVersion.get()
      project.dependencies.add("compileOnly", "io.papermc.paper:paper-api:${paperApiVersion}")
    }
  }
}
