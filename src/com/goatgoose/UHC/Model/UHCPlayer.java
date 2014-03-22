package com.goatgoose.uhc.Model;

import com.goatgoose.uhc.Tasks.PlayerFreezeTask;
import com.goatgoose.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class UHCPlayer {

    private UHC plugin;

    private Player player;

    private Team team = null;

    private boolean spectating = false;

    private boolean frozen = false;

    int episodeMarkInterval = 0; // in minutes

    public boolean initiatingScoreboard = false;

    public UHCPlayer(UHC instance, Player player) {
        plugin = instance;
        this.player = player;
        player.setScoreboard(plugin.getScoreboard());
        initiateHealthScoreboard();

        player.setGameMode(GameMode.SURVIVAL);
        for(UHCPlayer uhcPlayer : plugin.getUHCPlayers()) {
            uhcPlayer.getPlayer().showPlayer(player);
        }
    }

    public void setSpectating(boolean isSpectating) {
        spectating = isSpectating;
        if(spectating) {
            player.sendMessage("You are now spectating!");
            player.sendMessage("Remember to mute your mumble, or dedotaded will eat you!");
            player.setGameMode(GameMode.CREATIVE);
            for(UHCPlayer uhcPlayer : plugin.getUHCPlayers()) {
                uhcPlayer.getPlayer().hidePlayer(player);
            }
        } else {
            player.sendMessage("You are no longer spectating.");
            player.setGameMode(GameMode.SURVIVAL);
            for(UHCPlayer uhcPlayer : plugin.getUHCPlayers()) {
                uhcPlayer.getPlayer().showPlayer(player);
            }
        }
    }

    public void setTeam(Team team) {
        if(team == null) {
            this.team = null;
        } else {
            if(this.team == null) {
                this.team = team;
                this.team.addMember(this);
            } else if(this.team != team) {
                this.team.removeMember(this);
                this.team = team;
                this.team.addMember(this);
            }
        }
    }

    public void initiateHealthScoreboard() {
        initiatingScoreboard = true;
        player.damage(0.1);
        player.setHealth(20.0);
    }

    public void resetHealth() {
        player.setHealth(20.0);
        player.setFoodLevel(20);
    }

    public void deletePlayer() {
        plugin.removeUHCPlayer(this);
    }

    public String getNameWithColor() {
        if(team != null) {
            return team.getColor() + player.getName();
        } else {
            return player.getName();
        }
    }

    public Team getTeam() {
        return team;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isSpectating() {
        return spectating;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean isFrozen) {
        frozen = isFrozen;
        int id = 0;
        if(frozen) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            id = scheduler.scheduleSyncRepeatingTask(plugin, new PlayerFreezeTask(plugin, this, id), 0, 10);
        } else {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    public void setEpisodeMarkInterval(int interval) {
        episodeMarkInterval = interval;
    }

    public int getEpisodeMarkInterval() {
        return episodeMarkInterval;
    }

}
