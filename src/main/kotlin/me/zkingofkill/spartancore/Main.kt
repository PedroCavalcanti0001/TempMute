package me.zkingofkill.spartancore

import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class Main : JavaPlugin() {

    companion object {
        lateinit var singleton: Main
    }

    override fun onEnable() {
        singleton = this

        config.options().copyHeader(true)
        if (!File(dataFolder, "config.yml").exists()) {
            saveDefaultConfig()
        }
    }


}