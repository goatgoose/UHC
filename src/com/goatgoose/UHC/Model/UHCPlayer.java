package com.goatgoose.uhc.Model;

import com.goatgoose.uhc.Tasks.PlayerFreezeTask;
import com.goatgoose.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class UHCPlayer {

    private UHC plugin;

    private Player player;

    private Team team;

    private boolean spectating = false;

    private boolean frozen = false;

    int episodeMarkInterval = 0; // in minutes

    public UHCPlayer(UHC instance, Player player) {
        plugin = instance;
        this.player = player;
        setTeam(new Team(plugin, player.getName(), "GRAY"));
        player.setScoreboard(plugin.getScoreboard());
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
        if(this.team != null) {
            this.team.removeMember(this);
        }
        this.team = team;
        this.team.addMember(this);
    }

    public void resetTeam() {
        team = new Team(plugin, player.getName(), "GRAY");
        player.setPlayerListName(ChatColor.GRAY + player.getName());
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
        if(frozen) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncRepeatingTask(plugin, new PlayerFreezeTask(plugin, this), 0, 10);
        }
    }

    public void setEpisodeMarkInterval(int interval) {
        episodeMarkInterval = interval;
    }

    public int getEpisodeMarkInterval() {
        return episodeMarkInterval;
    }

}
