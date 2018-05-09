package de.olfillasodikno.rvgl.server.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtils {

	private static final String PREFIX = "[%s] %s";

	private LogUtils() {
	}

	public static void log(Logger logger, Level level, String name, String msg, Object... args) {
		String fullMsg = String.format(PREFIX, name, msg);
		logger.log(level, fullMsg, args);
	}
}
