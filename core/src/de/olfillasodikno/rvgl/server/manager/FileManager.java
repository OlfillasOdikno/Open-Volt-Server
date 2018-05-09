package de.olfillasodikno.rvgl.server.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import de.olfillasodikno.rvgl.server.Constants;
import de.olfillasodikno.rvgl.server.Server;
import de.olfillasodikno.rvgl.server.structures.Plugin;
import de.olfillasodikno.rvgl.server.structures.PluginConfig;
import de.olfillasodikno.rvgl.server.structures.Settings;
import de.olfillasodikno.rvgl.server.utils.LogUtils;

public class FileManager {

	private static final String LOG_NAME = "FileManager";

	private static final Logger logger = Logger.getLogger(FileManager.class.getName());

	public static final Gson gson = new Gson();
	public static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
	public static final JsonParser jsonParser = new JsonParser();

	public final File dataDir = new File("data");

	public final File settings = new File(dataDir, "settings.json");

	private final Server server;

	public FileManager(Server server) {
		this.server = server;
	}

	public void init() {
		if (!dataDir.exists()) {
			dataDir.mkdirs();
		}

		if (!settings.exists() || !loadSettings()) {
			saveSettings();
		}

		loadPlugins();
	}

	private void loadPlugins() {
		LogUtils.log(logger, Level.INFO, LOG_NAME, "Loading External Plugins..");
		Settings serverSettings = server.getSettings();
		File pluginDir = new File(serverSettings.getPluginDir());
		if (!pluginDir.exists()) {
			pluginDir.mkdirs();
		}
		byte[] buf = new byte[1024];
		File[] files = pluginDir.listFiles();
		for (File file : files) {
			if (file.isDirectory() || !file.getName().endsWith(".jar")) {
				continue;
			}
			try {
				JarFile jarFile = new JarFile(file);
				JarEntry plguinConfig = jarFile.getJarEntry(Constants.PLUGIN_CONFIG_FILE);
				if (plguinConfig != null) {
					PluginConfig config = loadConfig(jarFile, plguinConfig, pluginDir, buf, file);
					boolean ret = loadPlugin(config, jarFile, file);
					if (!ret) {
						LogUtils.log(logger, Level.SEVERE, LOG_NAME, "Failed to load Plugin: {0}", file.getName());
					}
				}
				jarFile.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e.getCause());
			}

		}
		LogUtils.log(logger, Level.INFO, LOG_NAME, "Finished Loading External Plugins!");
	}

	public PluginConfig reloadConfig(PluginConfig config) {
		byte[] buf = new byte[1024];

		Settings serverSettings = server.getSettings();
		File pluginDir = new File(serverSettings.getPluginDir());
		try {
			JarFile jarFile = new JarFile(config.getSource());
			JarEntry plguinConfig = jarFile.getJarEntry(Constants.PLUGIN_CONFIG_FILE);
			if (plguinConfig != null) {
				config = loadConfig(jarFile, plguinConfig, pluginDir, buf, config.getSource());
				jarFile.close();
				return config;
			}
			jarFile.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e.getCause());
		}
		return null;
	}

	public PluginConfig loadConfig(JarFile jarFile, JarEntry plguinConfig, File pluginDir, byte[] buf, File jar)
			throws IOException {
		JsonReader load = new JsonReader(
				new BufferedReader(new InputStreamReader(jarFile.getInputStream(plguinConfig))));
		JsonElement element = jsonParser.parse(load);
		PluginConfig config = prettyGson.fromJson(element, PluginConfig.class);
		config.setSource(jar);
		if (config.isHasExternalConfig()) {
			File configDir = new File(pluginDir, config.getName());
			if (!configDir.exists()) {
				configDir.mkdirs();
			}
			File configFile = new File(configDir, Constants.DEFAULT_CONFIG_FILE);
			JarEntry defaultConfig = jarFile.getJarEntry(Constants.DEFAULT_CONFIG_FILE);
			if (defaultConfig != null && !configFile.exists()) {
				InputStream is = jarFile.getInputStream(defaultConfig);
				FileOutputStream fos = new FileOutputStream(configFile);
				int r;
				while ((r = is.read(buf)) != -1) {
					fos.write(buf, 0, r);
				}
				is.close();
				fos.close();
			}
			JsonReader externLoad = new JsonReader(
					new BufferedReader(new InputStreamReader(new FileInputStream(configFile))));
			JsonElement externalElement = jsonParser.parse(externLoad);
			config.setExternalConfig(externalElement);
		}
		config.setGson(prettyGson);
		config.setJson(element);
		return config;
	}

	public boolean loadPlugin(PluginConfig config, JarFile jarFile, File file) {
		try (URLClassLoader loader = new URLClassLoader(new URL[] { file.toURI().toURL() },
				this.getClass().getClassLoader());) {
			ArrayList<String> classes = new ArrayList<>();
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry je = entries.nextElement();
				if (je.getName().endsWith(".class")) {
					classes.add(je.getName().split(".class")[0].replaceAll("/", "."));
				}
			}
			Class<?> main = loader.loadClass(config.getMain());
			if (Plugin.class.isAssignableFrom(main)) {
				Class<? extends Plugin> plugin = main.asSubclass(Plugin.class);
				server.getPluginManager().registerPlugin(plugin, config);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, e.getMessage(), e.getCause());
		}
		return false;
	}

	private boolean loadSettings() {
		try (JsonReader load = new JsonReader(new BufferedReader(new FileReader(settings)))) {
			Settings loadedSettings = prettyGson.fromJson(load, Settings.class);
			if (loadedSettings.getHash() != loadedSettings.hash()) {
				return false;
			}
			server.setSettings(loadedSettings);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e.getCause());
			return false;
		}
	}

	private void saveSettings() {
		try (PrintWriter save = new PrintWriter(new FileWriter(settings))) {
			Settings savedSettings = server.getSettings();
			save.println(prettyGson.toJson(savedSettings));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e.getCause());
		}
	}
}
