package com.goatgoose.uhc.Tasks;

import com.goatgoose.uhc.Model.UHCPlayer;
import com.goatgoose.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class CountdownTask extends BukkitRunnable {

    public UHC plugin;

    int countTo; // in seconds

    int counter;

    int id;

    public CountdownTask(UHC instance, int countTo, int id) {
        plugin = instance;
        this.countTo = countTo;
        this.counter = countTo;
        this.id = id;
    }

    @Override
    public void run() {
        if(plugin.getGamestate() == UHC.GameState.INGAME) {
            // temporary until Bukkit fixes "not yet scheduled" errors
            Bukkit.getScheduler().cancelTask(id);
            return;
        } else if(counter <= 0) {
            plugin.gameStart();
        } else {
            if(counter == countTo || counter == countTo/2 || counter <= 10) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "Game begins in " + counter + " seconds!");
            }
        }
        counter = counter - 1;
    }

}
