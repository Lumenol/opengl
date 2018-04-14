package com.example.kbourgeois.opengl.FloatK;

public class Float3 extends Float2 {

    public Float3() {
        this(0, 0, 0);
    }

    public Float3(Float3 float3) {
        super(float3);
    }

    public Float3(float x, float y, float z) {
        super(x, y, z);
    }

    protected Float3(float... value) {
        super(value);
    }

    final public void set(float x, float y, float z) {
        super.set(x, y, z);
    }

    final public float getZ() {
        return super.get(2);
    }

    final public void setZ(float z) {
        super.set(2, z);
    }
}
