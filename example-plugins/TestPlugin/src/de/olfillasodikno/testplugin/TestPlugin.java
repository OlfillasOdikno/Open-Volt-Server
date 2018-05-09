package de.olfillasodikno.testplugin;

import de.olfillasodikno.rvgl.server.structures.Plugin;

public class TestPlugin extends Plugin{

	@Override
	public void onEnable() {
		log("Config: ");
		log("{0}",getPluginConfig().getExternalConfig().toString());
	}

	@Override
	public void onDisable() {
		
	}

}
