package com.goatgoose.uhc.Tasks;

import com.goatgoose.uhc.Model.UHCPlayer;
import com.goatgoose.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerFreezeTask extends BukkitRunnable {

    public UHC plugin;

    UHCPlayer uhcPlayer;

    Location location;

    int episode = 1;

    int id;

    public PlayerFreezeTask(UHC instance, UHCPlayer uhcPlayer, int id) {
        plugin = instance;
        this.uhcPlayer = uhcPlayer;
        this.location = uhcPlayer.getPlayer().getLocation().getBlock().getLocation();
        this.id = id;
    }

    @Override
    public void run() {
        if(uhcPlayer.isFrozen()) {
            uhcPlayer.getPlayer().teleport(location);
        } else {
            Bukkit.getScheduler().cancelTask(id);
        }
    }
}
