package com.example.kbourgeois.opengl;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameObject {
    private Map<Class, MonoBehaviour> componants = new HashMap<>();
    private List<MonoBehaviour> nouveauxBehaviour = new ArrayList<>();

    private Transform transform = new Transform();

    final public Transform getTransform() {
        return transform;
    }

    final public MonoBehaviour getComponant(Class<? extends MonoBehaviour> c) {
        return componants.get(c);
    }

    final public void update(float dt) {
        for (Iterator<MonoBehaviour> iterator = nouveauxBehaviour.iterator(); iterator.hasNext(); ) {
            MonoBehaviour next = iterator.next();
            next.start();
            componants.put(next.getClass(), next);
        }
        nouveauxBehaviour.clear();
        Collection<MonoBehaviour> values = componants.values();
        for (Iterator<MonoBehaviour> iterator = values.iterator(); iterator.hasNext(); ) {
            MonoBehaviour next = iterator.next();
            next.update(dt);
        }
    }

    final public MonoBehaviour addComponant(Class<? extends MonoBehaviour> c) {
        try {
            Constructor<? extends MonoBehaviour> constructor = c.getConstructor(GameObject.class);
            MonoBehaviour monoBehaviour = constructor.newInstance(this);
            nouveauxBehaviour.add(monoBehaviour);
            return monoBehaviour;
        } catch (Exception e) {
            Log.e("addComponant", e.toString());
            return null;
        }
    }

}
