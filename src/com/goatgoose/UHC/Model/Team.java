package com.goatgoose.uhc.Model;

import com.goatgoose.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private UHC plugin;

    private String teamName;

    private ChatColor teamColor;

    private Score teamKills;

    private List<UHCPlayer> members = new ArrayList<UHCPlayer>();

    public Team(UHC instance, String teamName, String teamColor) {
        plugin = instance;
        plugin.addTeam(this);
        setTeamName(teamName);
        this.teamColor = ChatColor.valueOf(teamColor);

        teamKills = plugin.getTeamKills().getScore(Bukkit.getOfflinePlayer(this.teamColor + teamName));
    }

    public void teleportTeam(Location location) {
        for(UHCPlayer uhcPlayer : members) {
            uhcPlayer.getPlayer().teleport(location);
        }
    }

    public void setVisible() {
        teamKills.setScore(1);
        teamKills.setScore(0);
    }

    public void addMember(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();
        members.add(uhcPlayer);
        player.setPlayerListName(teamColor + player.getName());
    }

    public void removeMember(UHCPlayer uhcPlayer) {
        members.remove(uhcPlayer);
    }

    public List<UHCPlayer> getMembers() {
        return members;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void addTeamKill() {
        teamKills.setScore(teamKills.getScore() + 1);
    }

    public void setTeamKills(int teamKills) {
        this.teamKills.setScore(teamKills);
    }

    public int getTeamKills() {
        return teamKills.getScore();
    }
}
