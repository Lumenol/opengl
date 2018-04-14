package com.example.kbourgeois.opengl.FloatK;

import java.util.Arrays;
import java.util.Observable;

public abstract class FloatK extends Observable {
    private float[] value;

    protected FloatK(FloatK floatK) {
        this(floatK.getArray().clone());
    }

    protected FloatK(float... value) {
        this.value = value;
    }

    public float[] getArray() {
        return value;
    }

    final protected float get(int i) {
        return value[i];

    }

    public void set(FloatK floatk) {
        set(floatk.getArray());
    }

    final public void commit() {
        setChanged();
        super.notifyObservers();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatK floatK = (FloatK) o;
        return Arrays.equals(value, floatK.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public String toString() {
        return "FloatK{" +
                "value=" + Arrays.toString(value) +
                '}';
    }

    final protected void set(int i, float v) {
        value[i] = v;
        commit();
    }

    final protected void set(float... v) {
        for (int i = 0; i < Math.min(value.length,v.length); i++) {
            value[i] = v[i];
        }
        commit();
    }

}
