package com.mvrt.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StateHolder implements Iterable<Map.Entry<String, Object>> {
  private HashMap<String, Object> states = new HashMap<String, Object>();

  public void put(String key, Object value) {
    states.put(key, value);
  }

  public Object get(String key) {
    return states.get(key);
  }

  public Set<String> keySet() {
    return states.keySet();
  }

  @Override
  public Iterator<Map.Entry<String, Object>> iterator() {
    return states.entrySet().iterator();
  }
}
