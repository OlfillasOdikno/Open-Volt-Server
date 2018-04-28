package de.olfillasodikno.rvgl.server.structures;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Command {
	
	private final Method method;
	private final Plugin module;
	private final CommandInfo info;

	public Command( Method method, Plugin module,CommandInfo info) {
		this.method = method;
		this.module = module;
		this.info = info;
	}
	
	public Plugin getModule() {
		return module;
	}
	
	public Method getMethod() {
		return method;
	}
	
	public CommandInfo getInfo() {
		return info;
	}
	
	public String fire(String args,Player player) {
		try {
			method.setAccessible(true);
			Object obj = method.invoke(module,(Object)args,player);
			if(obj instanceof String) {
				return(String)obj;
			}
			return "["+info.name()+"] executed Command";
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return "["+info.name()+"] Failed to execute Command";
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface CommandInfo {
		String name();
		String permission();
		String[] syntax();
		String help();
	}
}
