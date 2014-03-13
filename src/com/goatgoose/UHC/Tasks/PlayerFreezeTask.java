package com.goatgoose.uhc.Tasks;

import com.goatgoose.uhc.Model.UHCPlayer;
import com.goatgoose.uhc.UHC;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerFreezeTask extends BukkitRunnable {

    public UHC plugin;

    UHCPlayer uhcPlayer;

    Location location;

    int episode = 1;

    public PlayerFreezeTask(UHC instance, UHCPlayer uhcPlayer) {
        plugin = instance;
        this.uhcPlayer = uhcPlayer;
        this.location = uhcPlayer.getPlayer().getLocation().getBlock().getLocation();
    }

    @Override
    public void run() {
        if(uhcPlayer.isFrozen()) {
            uhcPlayer.getPlayer().teleport(location);
        } else {
            cancel();
        }
    }

}
