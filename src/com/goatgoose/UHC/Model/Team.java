package com.goatgoose.uhc.Model;

import com.goatgoose.uhc.UHC;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private UHC plugin;

    private String name;

    private String prefix;

    private ChatColor color;

    private Score teamKills;

    private org.bukkit.scoreboard.Team vanillaTeam;

    private List<UHCPlayer> members = new ArrayList<UHCPlayer>();

    public Team(UHC instance, String name, String prefix, String color) {
        plugin = instance;
        plugin.addTeam(this);
        this.name = name;
        this.color = ChatColor.valueOf(color.toUpperCase());
        this.prefix = this.color + "[" + prefix + "] ";

        teamKills = plugin.getTeamKills().getScore(Bukkit.getOfflinePlayer(this.color + name));
        teamKills.setScore(0);
        setVisible();

        Scoreboard scoreboard = plugin.getScoreboard();
        vanillaTeam = scoreboard.registerNewTeam(name);
    }

    public void teleportTeam(Location location) {
        for(UHCPlayer uhcPlayer : members) {
            uhcPlayer.getPlayer().teleport(location);
        }
    }

    public void setVisible() {
        final int previousScore = teamKills.getScore();
        teamKills.setScore(1);
        teamKills.setScore(previousScore);
    }

    public void addMember(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();
        members.add(uhcPlayer);
        player.setPlayerListName(color + player.getName());
        player.setDisplayName(color + prefix + player.getName() + ChatColor.WHITE);
        vanillaTeam.addPlayer(Bukkit.getPlayer(player.getName()));
    }

    public void removeMember(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();
        members.remove(uhcPlayer);
        player.setPlayerListName(ChatColor.WHITE + player.getName());
        player.setDisplayName(ChatColor.WHITE + player.getName());
        vanillaTeam.removePlayer(Bukkit.getPlayer(player.getName()));
    }

    public void deleteTeam() {
        plugin.removeTeam(this);
        vanillaTeam.unregister();
    }

    public boolean isActive() {
        for(UHCPlayer uhcPlayer : getMembers()) {
            if(!uhcPlayer.isSpectating()) {
                return true;
            }
        }
        return false;
    }

    public List<UHCPlayer> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public void addKill() {
        teamKills.setScore(teamKills.getScore() + 1);
    }

    public void setKills(int teamKills) {
        this.teamKills.setScore(teamKills);
    }

    public int getTeamKills() {
        return teamKills.getScore();
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor chatColor) {
        color = chatColor;
    }
}
