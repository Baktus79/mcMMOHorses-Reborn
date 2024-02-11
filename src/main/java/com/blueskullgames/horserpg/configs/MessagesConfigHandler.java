package com.blueskullgames.horserpg.configs;

import com.blueskullgames.horserpg.HorseRPG;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MessagesConfigHandler {

	private final File file;
	private final FileConfiguration config;

	private BukkitTask saveTask;

	public MessagesConfigHandler(File mess) {
		this.file = mess;
		if (!this.file.exists())
			try {
				file.createNewFile();
			} catch (IOException ignored) {
				
			}
		config = YamlConfiguration.loadConfiguration(file);
	}

	public String a(String path, String a) {
		if (config.contains(path)) {
			return config.getString(path);
		}
		config.set(path, a);
		if (saveTask == null) {
			saveTask = new BukkitRunnable() {

				@Override
				public void run() {
					save();
					saveTask = null;
				}
			}.runTaskLater(HorseRPG.instance, 1);
		}
		return a;
	}

	public String[] b(String path, String[] a) {
		if (config.contains(path)) {
			List<String> k = config.getStringList(path);
			return k.toArray(new String[k.size()]);
		}
		config.set(path, a);
		if (saveTask == null) {
			saveTask = new BukkitRunnable() {

				@Override
				public void run() {
					save();
					saveTask = null;
				}
			}.runTaskLater(HorseRPG.instance, 1);
		}
		return a;
	}

	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			Bukkit.getLogger().severe(e.getMessage());
		}
	}
}
