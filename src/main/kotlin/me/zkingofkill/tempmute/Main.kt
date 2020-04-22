package me.zkingofkill.tempmute

import me.zkingofkill.tempmute.command.MuteCommand
import me.zkingofkill.tempmute.listener.ChatEvent
import me.zkingofkill.tempmute.model.MutedUser
import org.bukkit.plugin.java.JavaPlugin
import utils.ConfigurationFile
import java.io.File


class Main : JavaPlugin() {

    companion object {
        lateinit var singleton: Main
    }

    lateinit var users: ConfigurationFile


    override fun onEnable() {
        singleton = this
        config.options().copyHeader(true)
        getCommand("tempmute").executor = MuteCommand()
        users = ConfigurationFile(this, "users.yml","users.yml")
        users.save()
        MutedUser.init()
        server.pluginManager.registerEvents(ChatEvent(),this)


    }
}