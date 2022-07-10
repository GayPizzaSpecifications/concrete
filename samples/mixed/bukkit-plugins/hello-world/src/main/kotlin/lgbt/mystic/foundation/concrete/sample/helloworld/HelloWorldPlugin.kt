package lgbt.mystic.foundation.concrete.sample.helloworld

import lgbt.mystic.foundation.concrete.other.OtherLibrary
import org.bukkit.plugin.java.JavaPlugin

class HelloWorldPlugin : JavaPlugin() {
  override fun onEnable() {
    slF4JLogger.info("Enabled Hello World (Constant is ${OtherLibrary.SOME_CONSTANT})")
  }

  override fun onDisable() {
    slF4JLogger.info("Disabled Hello World (Constant is ${OtherLibrary.SOME_CONSTANT})")
  }
}
