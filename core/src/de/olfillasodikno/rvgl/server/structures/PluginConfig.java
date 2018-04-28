package de.olfillasodikno.rvgl.server.structures;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class PluginConfig {
	public String main;
	public String name;
	public String description;
	public String[] authors;
	public String version;

	public boolean has_external_config;

	public transient JsonElement json;
	public transient JsonElement externalConfig;
	public transient Gson gson;
	public transient File source;
	
	public static PluginConfig generate(Plugin plugin,String name, String description, String version, String... authors) {
		PluginConfig cfg = new PluginConfig();
		cfg.main = plugin.getClass().getName();
		cfg.name = name;
		cfg.authors = authors;
		cfg.description = description;
		cfg.version = version;
		return cfg;
	}
	
	public static PluginConfig generateDefault(Plugin plugin) {
		return generate(plugin, plugin.getClass().getSimpleName(), "", "", new String[] {});
	}
}
