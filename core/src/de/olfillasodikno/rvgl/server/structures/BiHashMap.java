package de.olfillasodikno.rvgl.server.structures;

import java.util.HashMap;

public class BiHashMap<K,V> {
	
	private HashMap<K, V> a = new HashMap<>();
	private HashMap<V, K> b = new HashMap<>();
	
	public void put(K key, V value) {
		a.put(key, value);
		b.put(value, key);
	}
	
	public V getV(K key) {
		return a.get(key);
	}
	
	public K getK(V value) {
		return b.get(value);
	}
	
	public boolean hasK(K key) {
		return a.containsKey(key);
	}
	
	public boolean hasV(V value) {
		return b.containsKey(value);
	}
	
	public void clear() {
		a.clear();
		b.clear();
	}
}
