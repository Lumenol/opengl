package com.example.kbourgeois.opengl.FloatK;

public class Float2 extends Float1 {

    public Float2() {
        this(0, 0);
    }


    public Float2(Float2 float2) {
        super(float2);
    }

    public Float2(float x, float y) {
        super(x, y);
    }

    protected Float2(float... value) {
        super(value);
    }

    final public void set(float x, float y) {
        super.set(x, y);
    }

    final public float getY() {
        return super.get(1);
    }

    final public void setY(float y) {
        super.set(1, y);
    }
}
