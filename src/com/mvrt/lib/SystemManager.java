package com.mvrt.lib;

import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SystemManager {
  private static SystemManager inst = null;

  private class StateMachineHolder {
    StateMachine machine;
    StateHolder stateHolder = new StateHolder();

    public StateMachineHolder(StateMachine sm) {
      machine = sm;
    }

    public StateHolder getStateHolder() {
      return stateHolder;
    }

    public void update() {
      machine.getState(stateHolder);
    }
  }

  private HashMap<String, StateMachineHolder> stateMachines;

  public static SystemManager getInstance() {
    if (inst == null) {
      inst = new SystemManager();
    }
    return inst;
  }

  public SystemManager() {
    this.stateMachines = new HashMap<String, StateMachineHolder>();
  }

  public void add(StateMachine sm) {
    StateMachineHolder smh = new StateMachineHolder(sm);
    stateMachines.put(sm.getName(), smh);
  }

  public void add(Collection<StateMachine> values) {
    for (StateMachine sm : values) {
      StateMachineHolder smh = new StateMachineHolder(sm);
      stateMachines.put(sm.getName(), smh);
    }
  }

  private void updateStates(String systemKey) {
    stateMachines.get(systemKey).update();
  }

  private void updateAllStates() {
    stateMachines.forEach((key, smh) -> smh.update());
  }

  private Object getValueForKey(String k) {
    String[] pieces = k.split(Pattern.quote("."));
    if (pieces.length != 2) {
      return null;
    }
    String base = pieces[0];
    String key = pieces[1];

    StateMachineHolder smh = stateMachines.get(base);
    if (smh == null) {
      return null;
    }

    StateHolder sh = smh.getStateHolder();

    if (sh == null) {
      return null;
    }

    return sh.get(key);
  }

  public JSONObject get() {
    JSONObject states = new JSONObject();

    updateAllStates();

    stateMachines.forEach((key, smh) -> smh.getStateHolder().forEach(
        (entry) -> states.put(key + "." + entry.getKey(), entry.getValue())));
    return states;
  }

  public JSONObject get(String k) {
    return get(new String[] {k});
  }

  public JSONObject get(String[] args) {
    updateAllStates();
    JSONObject states = new JSONObject();
    for (String k : args) {
      states.put(k, getValueForKey(k));
    }
    return states;
  }
}
