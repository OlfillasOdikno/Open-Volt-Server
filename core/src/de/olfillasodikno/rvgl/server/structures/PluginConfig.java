package de.olfillasodikno.rvgl.server.structures;

import java.io.File;
import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class PluginConfig implements Serializable{

	private static final long serialVersionUID = -6084631941438684926L;
	
	private String main;
	private String name;
	private String description;
	private String[] authors;
	private String version;

	private boolean hasExternalConfig;

	private transient JsonElement json;
	private transient JsonElement externalConfig;
	private transient Gson gson;
	private transient File source;

	public String getMain() {
		return main;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String[] getAuthors() {
		return authors;
	}

	public String getVersion() {
		return version;
	}

	public boolean isHasExternalConfig() {
		return hasExternalConfig;
	}

	public JsonElement getJson() {
		return json;
	}

	public JsonElement getExternalConfig() {
		return externalConfig;
	}

	public Gson getGson() {
		return gson;
	}

	public File getSource() {
		return source;
	}
	
	public void setSource(File source) {
		this.source = source;
	}
	
	public void setExternalConfig(JsonElement externalConfig) {
		this.externalConfig = externalConfig;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAuthors(String[] authors) {
		this.authors = authors;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setHasExternalConfig(boolean hasExternalConfig) {
		this.hasExternalConfig = hasExternalConfig;
	}

	public void setJson(JsonElement json) {
		this.json = json;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

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
		return generate(plugin, plugin.getClass().getSimpleName(), "", "");
	}
}
