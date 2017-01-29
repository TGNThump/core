package uk.me.pilgrim.dev.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Context {
	
	private final Map<Object, Object> locals = new HashMap<Object, Object>();
	
	public boolean containsKey(Object key){
		return locals.containsKey(key);
	}
	
	public boolean containsValue(Object value){
		return locals.containsValue(value);
	}
	
	public Object get(Object key){
		return locals.get(key);
	}
	
	public Optional<Object> getOpt(Object key){
		return Optional.ofNullable(get(key));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> key){
		return (T) locals.get(key);
	}
	
	public <T> Optional<T> getOpt(Class<T> key){
		return Optional.ofNullable(get(key));
	}
	
	public Object put(Object key, Object value){
		return locals.put(key, value);
	}
	
}
