package com.goatgoose.uhc.Tasks;

import com.goatgoose.uhc.UHC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EpisodeMarkerTask extends BukkitRunnable {

    public UHC plugin;

    Player player;

    int episode = 1;

    public EpisodeMarkerTask(UHC instance, Player player) {
        plugin = instance;
        this.player = player;
    }

    @Override
    public void run() {
        player.sendMessage(ChatColor.GOLD + "************** MARK END EPISODE " + episode + " **************");
        episode = episode + 1;
    }

}
