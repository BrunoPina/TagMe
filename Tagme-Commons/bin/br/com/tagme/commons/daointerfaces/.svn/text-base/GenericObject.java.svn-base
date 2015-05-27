package br.com.tagme.commons.daointerfaces;

import java.util.HashMap;
import java.util.Map;

public class GenericObject {

	private Map<String, Object>	data;

	public GenericObject() {
		data = new HashMap<String, Object>();
	}

	public void addValue(String key, String value) {
		data.put(key, value);
	}

	public Object getValue(String key) {
		return data.get(key);
	}
	
	public String getValueAsString(String key){
		return data.get(key).toString();
	}

	public boolean containsKey(String key) {
		return data.containsKey(key);
	}

	public void clear() {
		data.clear();
	}

}
