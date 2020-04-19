package me.zkingofkill.revolutionbosses

import inventory.InventoryManager
import me.zkingofkill.revolutionbosses.commands.BossCommand
import me.zkingofkill.revolutionbosses.data.Mysql
import me.zkingofkill.revolutionbosses.models.Boss
import me.zkingofkill.revolutionbosses.models.User
import me.zkingofkill.revolutionbosses.utils.tag
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class Main : JavaPlugin() {

    companion object {
        lateinit var singleton: Main
    }

    lateinit var inventoryManager: InventoryManager
    lateinit var mysql: Mysql

    override fun onEnable() {
        singleton = this
        config.options().copyHeader(true)
        if (!File(dataFolder, "config.yml").exists()) {
            saveDefaultConfig()
            reloadConfig()
        }

        inventoryManager = InventoryManager(this)
        inventoryManager.init()

        mysql = Mysql()
        mysql.init()

        User.delayedSaveAll()
        getCommand("bosses").executor = BossCommand()
        initHits()
    }

    override fun onDisable() {
        User.saveAll()
    }


    fun initHits() {
        val time = config.getInt("delayToHit")
        server.scheduler.runTaskTimer(this, {
            Bukkit.getOnlinePlayers()
                    .map { User.get(it.name) }
                    .filter { user -> user.equippedSword != null && user.selectedBoss != -1 && user.bosses.find { it.id == user.selectedBoss }!!.amount > 0 }
                    .forEach { user ->
                        val type = user.equippedSword!!.tag("typeSword")
                        val boss = Boss.byId(user.selectedBoss)

                        val damage = if (type == null) {
                            1.0
                        } else if (type.equals("hitkill", true)) {
                            boss.life
                        } else {
                            if (user.equippedSword!!.tag("damage") != null) {
                                user.equippedSword!!.tag("damage")!!.toDouble()
                            } else {
                                1.0
                            }
                        }
                        val selectedBoss = user.bosses.find { it.id == user.selectedBoss }!!
                        selectedBoss.life -= damage
                        if (selectedBoss.life <= 0.0) {
                            selectedBoss.amount -= 1
                            user.killedBosses += 1
                            selectedBoss.life = boss.life
                            boss.drops.forEach {
                                selectedBoss.drops.compute(it.id) { k, v ->
                                    v?.plus(1) ?: 1
                                }
                            }
                        }

                        val player = Bukkit.getPlayer(user.player)
                        if (player != null) {
                            inventoryManager.getInventory(player).ifPresent {
                                it.open(player)
                            }
                        }
                    }
        }, (20 * time).toLong(), (20 * time).toLong())
    }

}