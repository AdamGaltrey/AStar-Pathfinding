package com.adamki11s.path;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.adamki11s.pathing.AStar;
import com.adamki11s.pathing.AStar.InvalidPathException;
import com.adamki11s.pathing.PathingResult;
import com.adamki11s.pathing.Tile;

public class Pathfinding extends JavaPlugin {

	Location s = null, e = null;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {

			final Player p = (Player) sender;

			if (label.equalsIgnoreCase("path") || label.equalsIgnoreCase("p")) {

				if (args.length == 4 && args[0].equalsIgnoreCase("fall")) {
					double xv, yv, zv;
					xv = Double.parseDouble(args[1]);
					yv = Double.parseDouble(args[2]);
					zv = Double.parseDouble(args[3]);
					FallingBlock block = p.getWorld().spawnFallingBlock(p.getLocation(), Material.STONE, (byte) 0);
					Bukkit.broadcastMessage("§c" + xv + ", §a" + yv + ", §d" + zv);
					block.setVelocity(new Vector(xv, yv, zv));
					return true;
				}

				if (args[0].equalsIgnoreCase("mfall")) {
					for (int i = 0; i < 100; i++) {

						FallingBlock block = p.getWorld().spawnFallingBlock(p.getLocation(), Material.STONE, (byte) 0);
						float x = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
						float y = (float) -5 + (float) (Math.random() * ((5 - -5) + 1));
						float z = (float) -0.3 + (float) (Math.random() * ((0.3 - -0.3) + 1));
						Bukkit.broadcastMessage("§c" + x + ", §a" + y + ", §d" + z);
						block.setVelocity(new Vector(x, y, z));

					}
				}

				if (args.length == 1) {

					if (args[0].equalsIgnoreCase("current")) {
						p.sendMessage("Current standing on = " + p.getLocation().subtract(0, 1, 0).getBlock().getType().toString());
						return true;
					}
					if (args[0].equalsIgnoreCase("stop")) {
						this.getServer().getScheduler().cancelTasks(this);
						return true;
					} else if (args[0].equalsIgnoreCase("start")) {

						Location l = p.getLocation().subtract(0, 1, 0);
						s = l;
						if (l.getBlock().getTypeId() != 0) {
							s = l;
							p.sendMessage(ChatColor.GREEN + "Start point set");
						} else {
							p.sendMessage(ChatColor.RED + "Standing on ivalid end block = " + l.getBlock().getType().toString());
						}
						return true;
					} else if (args[0].equalsIgnoreCase("end")) {

						Location l = p.getLocation().subtract(0, 1, 0);
						e = l;
						if (l.getBlock().getTypeId() != 0) {
							e = l;
							p.sendMessage(ChatColor.GREEN + "End point set");
						} else {
							p.sendMessage(ChatColor.RED + "Standing on ivalid end block = " + l.getBlock().getType().toString());
						}
						return true;
					} else if (args[0].equalsIgnoreCase("bench")) {

						p.sendMessage("Running benchmark 30 times");

						s = new Location(p.getWorld(), 114, 95, 233);
						e = new Location(p.getWorld(), 90, 68, 221);

						Bukkit.getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {

							@Override
							public void run() {

								AStar path;

								long time = -System.currentTimeMillis();

								ArrayList<Tile> route = new ArrayList<Tile>();

								for (int i = 0; i < 30; i++) {
									if (i % 5 == 0) {
										p.sendMessage(i / 5 + "/6 runs completed");
									}
									try {
										path = new AStar(s, e, 3000);
										route = path.iterate();
									} catch (InvalidPathException e) {
										e.printStackTrace();
									}

								}

								time += System.currentTimeMillis();

								p.sendMessage(ChatColor.AQUA + "Average execution time out of 30 trials = " + time + " / 20 ms");
								p.sendMessage(ChatColor.AQUA + "= " + ((double) time / (double) 20) + "ms = " + (((double) time / (double) 20) / (double) 1000) + " seconds");

								if (route == null) {
									p.sendMessage(ChatColor.RED + "No path was found!");
								} else {
									p.sendMessage(ChatColor.GREEN + "Route was found!");
									for (Tile t : route) {
										p.sendBlockChange(new Location(p.getWorld(), (s.getBlockX() + t.getX()), (s.getBlockY() + t.getY()), (s.getBlockZ() + t.getZ())),
												Material.DIAMOND_BLOCK, (byte) 0);
									}

									p.sendBlockChange(s, Material.GOLD_BLOCK, (byte) 0);
									p.sendBlockChange(e, Material.GOLD_BLOCK, (byte) 0);
								}
							}

						});

						return true;

					} else if (args[0].equalsIgnoreCase("average")) {

						p.sendMessage("Going from start = " + s.getBlock().getType().toString() + " to end = " + e.getBlock().getType().toString());

						p.sendBlockChange(s, Material.GOLD_BLOCK, (byte) 0);
						p.sendBlockChange(e, Material.GOLD_BLOCK, (byte) 0);

						p.sendBlockChange(new Location(s.getWorld(), s.getBlockX(), s.getBlockY() + 3, s.getBlockZ()), Material.GOLD_BLOCK, (byte) 0);
						p.sendBlockChange(new Location(s.getWorld(), e.getBlockX(), e.getBlockY() + 3, e.getBlockZ()), Material.GOLD_BLOCK, (byte) 0);

						if (s == null) {
							p.sendMessage(ChatColor.RED + "Start location is null");
							return true;
						} else if (e == null) {
							p.sendMessage(ChatColor.RED + "End location is null");
							return true;
						}

						Bukkit.getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {

							@Override
							public void run() {

								p.sendMessage(ChatColor.GREEN + "Starting route trace...");

								AStar path;

								long time = -System.currentTimeMillis();

								ArrayList<Tile> route = new ArrayList<Tile>();

								for (int i = 0; i < 20; i++) {

									try {
										path = new AStar(s, e, 3000);
										route = path.iterate();
									} catch (InvalidPathException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

								time += System.currentTimeMillis();

								p.sendMessage(ChatColor.AQUA + "Average execution time out of 20 trials = " + time + " / 20 ms");
								p.sendMessage(ChatColor.AQUA + "= " + ((double) time / (double) 20) + "ms = " + (((double) time / (double) 20) / (double) 1000) + " seconds");

								if (route == null) {
									p.sendMessage(ChatColor.RED + "No path was found!");
								} else {
									p.sendMessage(ChatColor.GREEN + "Route was found!");
									for (Tile t : route) {
										p.sendBlockChange(new Location(p.getWorld(), (s.getBlockX() + t.getX()), (s.getBlockY() + t.getY()), (s.getBlockZ() + t.getZ())),
												Material.DIAMOND_BLOCK, (byte) 0);
									}
								}
							}

						});

						return true;

					} else if (args[0].equalsIgnoreCase("find")) {

						p.sendMessage("Going from start = " + s.getBlock().getType().toString() + " to end = " + e.getBlock().getType().toString());

						p.sendBlockChange(new Location(s.getWorld(), s.getBlockX(), s.getBlockY() + 3, s.getBlockZ()), Material.GOLD_BLOCK, (byte) 0);
						p.sendBlockChange(new Location(s.getWorld(), e.getBlockX(), e.getBlockY() + 3, e.getBlockZ()), Material.GOLD_BLOCK, (byte) 0);

						if (s == null) {
							p.sendMessage(ChatColor.RED + "Start location is null");
							return true;
						} else if (e == null) {
							p.sendMessage(ChatColor.RED + "End location is null");
							return true;
						}

						Bukkit.getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {

							@Override
							public void run() {

								p.sendMessage(ChatColor.GREEN + "Starting route trace...");

								AStar path;
								try {
									// range of 10
									path = new AStar(s, e, (int) (s.distance(e) * 3));

									long time = -System.currentTimeMillis();

									ArrayList<Tile> route = path.iterate();
									PathingResult res = path.getPathingResult();

									switch (res) {
									case SUCCESS:
										// path was successfull
										p.sendMessage(ChatColor.BLUE + "SUCCESS");
										break;
									case NO_PATH:
										// no path found
										p.sendMessage(ChatColor.BLUE + "NO_PATH");
										break;
									}

									time += System.currentTimeMillis();

									p.sendMessage(ChatColor.AQUA + "Code executed in " + time + " ms");

									if (route == null) {
										p.sendMessage(ChatColor.RED + "No path was found!");
									} else {
										p.sendMessage(ChatColor.GREEN + "Route was found!");
										int start = 0;
										for (Tile t : route) {
											start++;
											if (start > 5) {
												p.sendBlockChange(new Location(p.getWorld(), (s.getBlockX() + t.getX()), (s.getBlockY() + t.getY()), (s.getBlockZ() + t.getZ())),
														Material.DIAMOND_BLOCK, (byte) 0);
											} else {
												p.sendBlockChange(new Location(p.getWorld(), (s.getBlockX() + t.getX()), (s.getBlockY() + t.getY()), (s.getBlockZ() + t.getZ())),
														Material.GOLD_BLOCK, (byte) 0);
											}
										}
									}
								} catch (InvalidPathException e) {
									System.out.println(e.getErrorReason());
									if (e.isStartNotSolid()) {
										System.out.println("End was air");
									}
									if (e.isStartNotSolid()) {
										System.out.println("Start was air");
									}
									// e.printStackTrace();
								}

							}

						});

						return true;
					}
				}
			}

		}
		return true;
	}

}
