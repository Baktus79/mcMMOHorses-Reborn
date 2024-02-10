package com.blueskullgames.horserpg.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("deprecation")
public class GithubDependDownloader {

	public static boolean autoUpdate(final Plugin main, final File output, String author, String githubProject,
			String jarname) {
		try {

			String tagname;
			URL api = new URL("https://api.github.com/repos/" + author + "/" + githubProject + "/releases/latest");
			URLConnection con = api.openConnection();
			con.setConnectTimeout(15000);
			con.setReadTimeout(15000);

			JsonObject json = new JsonParser().parse(new InputStreamReader(con.getInputStream())).getAsJsonObject();
			tagname = json.get("tag_name").getAsString();

			final URL download = new URL("https://github.com/" + author + "/" + githubProject + "/releases/download/"
					+ tagname + "/" + jarname);

			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Found a dependancy with version id " + ChatColor.WHITE + tagname
					+ ChatColor.LIGHT_PURPLE + ". downloading now!!");

			new BukkitRunnable() {

				@Override
				public void run() {
					try {

						InputStream in = download.openStream();
						output.setWritable(true, false);
						output.delete();

						copy(in, new FileOutputStream(output));

						new BukkitRunnable() {
							public void run() {
								Bukkit.reload();
							}
						}.runTaskLater(main, 4);
					} catch (IOException e) {
						Bukkit.getLogger().severe(e.getMessage());
					}
				}
			}.runTaskLaterAsynchronously(main, 0);
			return true;
		} catch (IOException e) {
			Bukkit.getLogger().severe(e.getMessage());
		}
		return false;
	}

	private static long copy(InputStream in, OutputStream out) throws IOException {
		long bytes = 0;
		byte[] buf = new byte[0x1000];
		while (true) {
			int r = in.read(buf);
			if (r == -1)
				break;
			out.write(buf, 0, r);
			bytes += r;
		}
		out.flush();
		out.close();
		in.close();
		return bytes;
	}

}
