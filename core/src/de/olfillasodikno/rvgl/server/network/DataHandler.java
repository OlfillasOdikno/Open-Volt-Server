package de.olfillasodikno.rvgl.server.network;

public interface DataHandler<K,V,T> {
	
	public T decode(K data, boolean handle) throws InstantiationException, IllegalAccessException;
	
	public V encode(T obj);
	
	public void handle(K orig,T obj);

}
