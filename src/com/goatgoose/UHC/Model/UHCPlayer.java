package com.goatgoose.uhc.Model;

import com.goatgoose.uhc.UHC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class UHCPlayer {

    private UHC plugin;

    private Player player;

    private boolean spectating = false;

    private Team team;

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
            for(UHCPlayer uhcPlayer : plugin.getUhcPlayers()) {
                uhcPlayer.getPlayer().hidePlayer(player);
            }
        } else {
            player.sendMessage("You are no longer spectating.");
            player.setGameMode(GameMode.SURVIVAL);
            for(UHCPlayer uhcPlayer : plugin.getUhcPlayers()) {
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

    public Team getTeam() {
        return team;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isSpectating() {
        return spectating;
    }

}
