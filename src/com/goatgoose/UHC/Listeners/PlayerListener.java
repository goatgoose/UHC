package com.goatgoose.uhc.Listeners;

import com.goatgoose.uhc.Model.UHCPlayer;
import com.goatgoose.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

public class PlayerListener implements Listener {

    private UHC plugin;

    public PlayerListener(UHC instance) {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.addUHCPlayer(new UHCPlayer(plugin, event.getPlayer()));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        UHCPlayer uhcPlayer = plugin.getUHCPlayer(event.getEntity());
        uhcPlayer.setSpectating(true);
        UHCPlayer killer = plugin.getUHCPlayer(event.getEntity().getKiller());
        killer.getTeam().addTeamKill();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        UHCPlayer uhcPlayer = plugin.getUHCPlayer(event.getPlayer());
        if(uhcPlayer.isSpectating()) {
            event.setCancelled(true);
        }
    }

}
