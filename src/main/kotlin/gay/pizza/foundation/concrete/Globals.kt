package gay.pizza.foundation.concrete

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object Globals {
  val gsonPretty: Gson = GsonBuilder().setPrettyPrinting().create()
  val gson: Gson = Gson()
}
