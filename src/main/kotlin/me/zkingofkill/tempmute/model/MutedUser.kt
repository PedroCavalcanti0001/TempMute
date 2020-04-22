package me.zkingofkill.tempmute.model

import me.zkingofkill.tempmute.Main
import org.bukkit.Bukkit

class MutedUser(var playerName: String, var muteTime: Long = System.currentTimeMillis(), var duration: Int) {

    private fun timeLeft(): Double {
        var f = -1.0
        val now = System.currentTimeMillis()
        val r = (now - this.muteTime).toInt() / 1000
        f = ((r - this.duration) * -1).toDouble()
        return f
    }

    fun mute() {
        Main.singleton.users.set("$playerName.time", muteTime)
        Main.singleton.users.set("$playerName.duration", duration)
        Main.singleton.users.save()
    }

    fun unMute() {
        Main.singleton.users.set(playerName, null)
        Main.singleton.users.save()
    }


    companion object {

        fun init() {
            Main.singleton.server.scheduler.runTaskTimer(Main.singleton, {
                list().forEach {
                    if (it.timeLeft() <= 0.0) {
                        it.unMute()
                        Bukkit.getPlayer(it.playerName)?.sendMessage("§aVocê pode falar novamente!")
                    }
                }
            }, 50, 50)
        }

        fun list(): ArrayList<MutedUser> {
            val list = arrayListOf<MutedUser>()
            Main.singleton.users.getConfigurationSection("").getKeys(false).forEach {
                val time = Main.singleton.users.getLong("$it.time")
                val duration = Main.singleton.users.getInt("$it.duration")
                list.add(MutedUser(it, time, duration))
            }
            return list
        }

        fun find(playerName: String): MutedUser? {
            return list().find { it.playerName == playerName }
        }
    }
}