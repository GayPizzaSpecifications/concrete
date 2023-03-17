plugins {
  id("gay.pizza.foundation.concrete-base")
}

concreteItem {
  type.set("library")
  fileInclusion {
    tasks.jar.get().outputs.files.associateWith { "library-jar" }
  }
}
