package com.goatgoose.uhc;

import com.goatgoose.uhc.Listeners.PlayerListener;
import com.goatgoose.uhc.Model.Team;
import com.goatgoose.uhc.Model.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;

public class UHC extends JavaPlugin {

    private PlayerListener playerListener;

    private List<UHCPlayer> uhcPlayers = new ArrayList<UHCPlayer>();

    private List<Team> teams = new ArrayList<Team>();

    private ScoreboardManager scoreboardManager;

    private Scoreboard scoreboard;

    private Objective teamKills;

    @Override
    public void onEnable() {
        playerListener = new PlayerListener(this);

        scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();

        teamKills = scoreboard.registerNewObjective("teamKills", "dummy");

        teamKills.setDisplaySlot(DisplaySlot.SIDEBAR);
        teamKills.setDisplayName("Team Kills");
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        Player player = (Player) sender;
        UHCPlayer uhcPlayer = getUHCPlayer(player);
        if(uhcPlayer == null) {
            return false;
        }
        if(command.getName().equalsIgnoreCase("team")) {
            if(args[0].equalsIgnoreCase("create")) {
                if(args.length != 3) {
                    sender.sendMessage("Incorrect usage!  /team create [Team Name] [Team Color]");
                    return false;
                } else {
                    Team newTeam = new Team(this, args[1], args[2]);
                    teams.add(newTeam);
                    newTeam.setVisible();
                    sender.sendMessage("Team " + args[1] + " created!");
                    return true;
                }
            } else if(args[0].equalsIgnoreCase("addPlayer")) {
                if(args.length != 3) {
                    sender.sendMessage("Incorrect usage!  /team addPlayer [Team] [Player]");
                    return false;
                } else {
                    UHCPlayer playerToAdd = getUHCPlayer(Bukkit.getPlayer(args[2]));
                    if(playerToAdd == null) {
                        sender.sendMessage("No player by that name found!  /team addPlayer [Team] [Player]");
                        return false;
                    }
                    Team team = getTeam(args[1]);
                    if(team == null) {
                        sender.sendMessage("No team by that name found!  /team addPlayer [Team] [Player]");
                        return false;
                    } else {
                        playerToAdd.setTeam(team);
                        sender.sendMessage("Player " + playerToAdd.getPlayer().getName() + " added to " + team.getTeamName() + "!");
                        return true;
                    }
                }
            } else if(args[0].equalsIgnoreCase("setScore")) {
                if(args.length != 3) {
                    sender.sendMessage("Incorrect usage! /team setScore [Team] [Score]");
                    return false;
                } else {
                    Team team = getTeam(args[1]);
                    if(team == null) {
                        sender.sendMessage("No team by that name found!  /team setScore [Team] [Score]");
                        return false;
                    }
                    try {
                        int score = Integer.parseInt(args[2]);
                        team.setTeamKills(score);
                        return true;
                    } catch(Exception e) {
                        sender.sendMessage("Invalid score! /team setScore [Team] [Score]");
                        return false;
                    }
                }
            } else if(args[0].equalsIgnoreCase("spreadTeams")) {
                if(args.length != 2) {
                    sender.sendMessage("Invalid usage! /spreadTeams [Spread Radius]");
                    return false;
                } else {
                    int spreadRadius = 0;
                    try {
                        spreadRadius = Integer.parseInt(args[1]);
                    } catch(Exception e) {
                        sender.sendMessage("Invalid spread radius! /spreadTeams [Spread Radius]");
                        return false;
                    }
                    Location spawn = player.getWorld().getSpawnLocation();
                    for(int i = 0; i < teams.size(); i++) {
                        Team team = teams.get(i);

                        double spreadX = spawn.getBlockX() + spreadRadius * Math.cos((2 * Math.PI * i) / teams.size());
                        double spreadZ = spawn.getBlockZ() + spreadRadius * Math.sin((2 * Math.PI * i) / teams.size());

                        Location spreadLocation = new Location(player.getWorld(), spreadX, getGround(player.getWorld(), spreadX, spreadZ), spreadZ);
                        team.teleportTeam(spreadLocation);
                    }
                }
            }
        }
        if(command.getName().equalsIgnoreCase("spectate")) {
            if(args.length == 0) {
                if(uhcPlayer.isSpectating()) {
                    uhcPlayer.setSpectating(false);
                } else {
                    uhcPlayer.setSpectating(true);
                }
                return true;
            } else if(args.length == 2) {
                UHCPlayer target = getUHCPlayer(Bukkit.getPlayer(args[0]));
                if(target == null) {
                    sender.sendMessage("No player by that name found!  /team spectate [Player]");
                    return false;
                }
                if(target.isSpectating()) {
                    target.setSpectating(false);
                    sender.sendMessage("Player " + target.getPlayer().getName() + " is no longer spectating.");
                } else {
                    target.setSpectating(true);
                    sender.sendMessage("Player " + target.getPlayer().getName() + " is now spectating!");
                }
            } else {
                sender.sendMessage("Invalid usage! /spectate [Player]");
            }
            return false;
        }
        return false;
    }

    public int getGround(World world, double x, double z) {
        for(int y = world.getMaxHeight(); y > 0; y = y - 1) {
            Location location = new Location(world, x, y, z);
            if(location.getBlock().getType() != Material.AIR) {
                return y;
            }
        }
        return 0;
    }

    public void addUHCPlayer(UHCPlayer uhcPlayer) {
        uhcPlayers.add(uhcPlayer);
    }

    public UHCPlayer getUHCPlayer(Player player) {
        for(UHCPlayer uhcPlayer : uhcPlayers) {
            if(uhcPlayer.getPlayer().equals(player)) {
                return uhcPlayer;
            }
        }
        return null;
    }

    public List<UHCPlayer> getUhcPlayers() {
        return uhcPlayers;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getTeamKills() {
        return teamKills;
    }

    public Team getTeam(String teamName) {
        for(Team team : teams) {
            if(team.getTeamName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}
