package de.olfillasodikno.testplugin;

import de.olfillasodikno.rvgl.server.structures.Plugin;

public class TestPlugin extends Plugin{

	@Override
	public void onEnable() {
		log("Config: ");
		log(getPluginConfig().externalConfig.toString());
	}

	@Override
	public void onDisable() {
		
	}

}
