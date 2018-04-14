package com.example.kbourgeois.opengl.FloatK;

public class Float4 extends Float3 {

    public Float4() {
        this(0, 0, 0, 0);
    }

    public Float4(Float4 float4) {
        super(float4);
    }

    public Float4(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    protected Float4(float... value) {
        super(value);
    }

    final public void set(float x, float y, float z, float w) {
        super.set(x, y, z, w);
    }

    final public float getW() {
        return super.get(3);
    }

    final public void setW(float w) {
        super.set(3, w);
    }
}
