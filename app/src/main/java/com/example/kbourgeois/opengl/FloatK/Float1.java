package com.example.kbourgeois.opengl.FloatK;

public class Float1 extends FloatK {

    public Float1(Float1 float1) {
        super(float1);
    }

    public Float1(float x) {
        super(x);
    }

    public Float1() {
        this(0);
    }

    protected Float1(float... value) {
        super(value);
    }

    final public void set(float x) {
        super.set(x);
    }

    final public float getX() {
        return super.get(0);
    }

    final public void setX(float x) {
        super.set(0, x);
    }
}
