package com.goatgoose.uhc.Listeners;

import com.goatgoose.uhc.Model.UHCPlayer;
import com.goatgoose.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.*;

public class PlayerListener implements Listener {

    private UHC plugin;

    public PlayerListener(UHC instance) {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(plugin.getUHCPlayer(event.getPlayer()) == null) {
            plugin.addUHCPlayer(new UHCPlayer(plugin, event.getPlayer()));
        }
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
        if(uhcPlayer.isSpectating() || plugin.getGamestate() == UHC.GameState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(plugin.getGamestate() == UHC.GameState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(plugin.getGamestate() == UHC.GameState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAchievementGet(PlayerAchievementAwardedEvent event) {
        if(plugin.getGamestate() == UHC.GameState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        UHCPlayer uhcPlayer = plugin.getUHCPlayer(event.getPlayer());
        if(uhcPlayer.isFrozen()) {
            event.setCancelled(true);
        }
    }

}
