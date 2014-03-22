package com.goatgoose.uhc.Listeners;

import com.goatgoose.uhc.Model.*;
import com.goatgoose.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

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
        Team team = uhcPlayer.getTeam();
        uhcPlayer.setSpectating(true);
        UHCPlayer killer = plugin.getUHCPlayer(event.getEntity().getKiller());
        if(killer != null) {
            killer.getTeam().addKill();

            //untested
            ItemStack skull = new ItemStack(Material.SKULL, 1);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            skullMeta.setOwner(killer.getPlayer().getName());
            skull.setItemMeta(skullMeta);
            killer.getPlayer().getWorld().dropItemNaturally(killer.getPlayer().getLocation(), skull);
        }

        if(team.isActive()) {
            Bukkit.broadcastMessage(team.getColor() + team.getName().toUpperCase() + ChatColor.GOLD + " HAS BEEN ELIMINATED");
            team.deleteTeam();
        }

        if(plugin.getActiveTeams().size() <= 1) {
            plugin.setGamestate(UHC.GameState.LOBBY);
            Team winningTeam = plugin.getTeams().get(0);
            Bukkit.broadcastMessage(winningTeam.getColor() + winningTeam.getName().toUpperCase() + ChatColor.GOLD + " HAS WON UHC");
        }
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
        if(event.getEntity() instanceof Player) {
            UHCPlayer uhcPlayer = plugin.getUHCPlayer((Player) event.getEntity());
            if(uhcPlayer.initiatingScoreboard) {
                uhcPlayer.initiatingScoreboard = false;
            } else {
                if(plugin.getGamestate() == UHC.GameState.LOBBY) {
                    event.setCancelled(true);
                }
            }
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
}
