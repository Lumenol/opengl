package com.example.kbourgeois.opengl;

import com.example.kbourgeois.opengl.FloatK.Float3;

import java.util.Observer;

public interface Bounds {

    void addObserver(Observer observer);

    Float3 getSize();

    void setParent(Transform transform);

    Float3 getCenter();

    Float3 getMin();

    Float3 getMax();

    public Float3 getLocalMin();

    public Float3 getLocalMax();

    Float3 getLocalCenter();
}
