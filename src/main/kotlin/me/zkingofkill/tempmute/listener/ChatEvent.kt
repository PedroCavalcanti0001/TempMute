package me.zkingofkill.tempmute.listener

import me.zkingofkill.tempmute.model.MutedUser
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatEvent : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onChat(event: AsyncPlayerChatEvent) {
        val mutedUser = MutedUser.find(event.player.name)
        if (mutedUser != null) {
            event.isCancelled = true
            event.player.sendMessage("§cVocê não pode falar no chat pois está mutado!")
        }
    }
}