package me.zkingofkill.tempmute.command

import me.zkingofkill.tempmute.model.MutedUser
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MuteCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("tempmute.admin")) return false
        if (args.isEmpty()) {
            sender.sendMessage("§c/tempmute <player> <duration>")
            sender.sendMessage("§c/tempmute remove <player>")
        } else {
            if (args[0].equals("remove", true)) {
                if (args.size >= 2) {
                    val mutedUser = MutedUser.find(args[1])
                    if (mutedUser != null) {
                        mutedUser.unMute()
                        sender.sendMessage("§cUsuario desmutado com sucesso!")
                    }
                } else {
                    sender.sendMessage("§c/mute remove <player>")
                }
            } else if (Bukkit.getPlayer(args[0]) != null) {
                if (args.size >= 2) {
                    val mutedUser = MutedUser(playerName = args[0], duration = args[1].toInt())
                    mutedUser.mute()
                    sender.sendMessage("§cUsuario mutado com sucesso!")
                    Bukkit.getPlayer(args[0]).sendMessage("§cVocê foi mutado por ${sender.name}!")
                } else {
                    sender.sendMessage("§c/mute <player> <duration>")
                }
            } else {
                sender.sendMessage("§cPlayer não encontrado!")
            }
        }
        return false
    }
}